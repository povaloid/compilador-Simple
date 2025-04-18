package simple;
import javax.sound.midi.SysexMessage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// import static simple.TokenTipo.*;

class Scanner {
    private static class ScannerError extends RuntimeException {}
    private volatile Token ultimoToken;
    private final String source;
    private final List<Token> tokens = new ArrayList<>();
    private int start = 0;
    private int current = 0;
    private int line = 1;
    private int currentLineNumber = 1;
    private int lastKnownLineNumber = 1; // New field to store the last known line number
    private boolean foundEnd = false; // Flag to track if 'end' is encountered

    private static final Map<String, TokenTipo> keywords;

    static {
        keywords = new HashMap<>();
        keywords.put("rem", TokenTipo.COMENTARIO);
        keywords.put("input", TokenTipo.INPUT);
        keywords.put("let", TokenTipo.LET);
        keywords.put("print", TokenTipo.PRINT);
        keywords.put("goto", TokenTipo.GOTO);
        keywords.put("if", TokenTipo.IF);
        keywords.put("end", TokenTipo.END);
    }

    Scanner(String source) {
        this.source = source;
    }

    List<List<Token>> formalizarLinhasDeInstrucao(List<Token> tokens) {
        List<List<Token>> programa = new ArrayList<>();
        List<Token> linhaEmLeitura = new ArrayList<>();

        for (Token token : tokens) {
            if (token.tipo == TokenTipo.FIM_DE_TEXTO /*|| token.tipo == TokenTipo.END */) {
                //linhaEmLeitura.add(token);
                programa.add(definirLinha(linhaEmLeitura));
                linhaEmLeitura.clear();
            } else {
                linhaEmLeitura.add(token);
            }
        }

        return programa;
    }

    List<Token> scanTokens() {
        addToken(TokenTipo.NOVA_LINHA,null); // Adiciona token auxiliar para começar o scan
        tokens.remove(0);

        while (!estaNoFinal()) {
            start = current;
            scanToken();
        }
        if (!foundEnd) {
            System.err.println("O programa não contém a instrução end.");
            System.exit(61);
        }
        return tokens;
    }

    private void scanToken() {
        char c = avancar();

        switch (c) {
            case '+': addToken(TokenTipo.ADICAO); break;
            case '-':
                if (isNegativeConstant()) {
                    number(true);  // Treat as a negative number
                } else {
                    addToken(TokenTipo.SUBTRACAO);  // Treat as subtraction
                }
                break;
            case '*': addToken(TokenTipo.MULTIPLICACAO); break;
            case '/': addToken(TokenTipo.DIVISAO); break;
            case '%': addToken(TokenTipo.RESTO_DIVISAO); break;
            case '=':
                addToken(match('=') ? TokenTipo.IGUAL_A : TokenTipo.ATRIBUICAO);
                break;
            case '>':
                addToken(match('=') ? TokenTipo.MAIOR_IGUAL : TokenTipo.MAIOR);
                break;
            case '<':
                addToken(match('=') ? TokenTipo.MENOR_IGUAL : TokenTipo.MENOR);
                break;
            case ' ':
            case '\r':
            case '\t':
                // Ignore whitespace.
                break;
            case '\n':
                line++;
                addToken(TokenTipo.FIM_DE_TEXTO);
                updateLastKnownLineNumber(lastKnownLineNumber); // Update last known line number
                break;
            default:
                if (ehNumero(c)) {
                    number(false);
                } else if (ehLetra(c)) {
                    identifier();
                } else {
                    Simple.error(line, "Unexpected character.");
                }
        }
    }

    // Helper method to determine if '-' should be part of a negative constant
    private boolean isNegativeConstant() {
        if (!estaNoFinal()) {
            char nextChar = consultaProx();
            return ehNumero(nextChar); // If next character is a digit, it's a negative constant
        }
        return false;
    }

    // Overloaded number method to handle negative constants
    private void number(boolean isNegative) {
        while (ehNumero(consultaProx())) avancar();
        String numberText = source.substring(start, current);
        double numberValue = Double.parseDouble(numberText);

        if (isNegative) numberValue = -numberValue; // Convert to negative if needed

        // Se ultimo token == FDT, adiciona nova linha com o primeiro numeral da linha
        if (ultimoToken.tipo == TokenTipo.FIM_DE_TEXTO || tokens.isEmpty()) {
            if (numberValue < lastKnownLineNumber) {
                int linhaEmInteiro = (int) numberValue;
                // error(ultimoToken, "A ordenação das linhas deve ser crescente");
            }
            currentLineNumber = (int) numberValue;
            addToken(TokenTipo.NOVA_LINHA, currentLineNumber);
        } else {
            addToken(TokenTipo.CONSTANTE_NUMERICA, numberValue);
        }

        // Update last known line number
        updateLastKnownLineNumber(currentLineNumber);
    }


    private void identifier() {
        while (ehLetraNumero(consultaProx())) avancar();
        String text = source.substring(start, current);
        TokenTipo type = keywords.get(text);
        if (type == TokenTipo.COMENTARIO) {
            capturaComentarios(); // Skip the rest of the comment
        } else if (type == null) {
            type = TokenTipo.VARIAVEL;
        } else if (type == TokenTipo.END) {
            foundEnd = true; // Set flag to true if 'end' is found
        }
        addToken(type);
    }

    private boolean match(char expected) {
        if (estaNoFinal()) return false;
        if (source.charAt(current) != expected) return false;
        current++;
        return true;
    }

    private char consultaProx() {
        if (estaNoFinal()) return '\0';
        return source.charAt(current);
    }

    private boolean ehLetra(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    private boolean ehLetraNumero(char c) {
        return ehLetra(c) || ehNumero(c);
    }

    private boolean ehNumero(char c) {
        return c >= '0' && c <= '9';
    }

    private boolean estaNoFinal() {
        return current >= source.length();
    }

    private char avancar() {
        return source.charAt(current++);
    }

    private void addToken(TokenTipo type) {
        addToken(type, null);
    }

    private void capturaComentarios() {
        while (!estaNoFinal() && consultaProx() != '\n') {
            avancar(); // Consume characters until end of line
        }
    }

    private void addToken(TokenTipo type, Object literal) {
        String text = source.substring(start, current);

        // if (type == FIM_DE_TEXTO) { text = null; };

        tokens.add(new Token(type, text, literal, currentLineNumber));
        ultimoToken = new Token(type, text, literal, currentLineNumber);
    }

    private void updateLastKnownLineNumber(int lineNumber) {
        if (lineNumber > lastKnownLineNumber) {
            lastKnownLineNumber = lineNumber;
        }
    }

    private Scanner.ScannerError error(Token token, String message) {
        Simple.error(token, message);
        return new Scanner.ScannerError();
    }

    List<Token> definirLinha(List<Token> tkns) {
        return new ArrayList<>(tkns);
    }

}
