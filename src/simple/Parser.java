package simple;

import java.util.List;
import static simple.TokenTipo.*;

/*
[ Arvore de derivacao ] - Expressões

*    Programa        →   Linha Programa | fim de texto

*    Linha           →   NúmeroLinha Instrução delimitador nova linha

*    Instrução       →   Comentario | Atribuicao | EntradaSaida | Goto | Condicional | Finaliza

*    Comentario      →   rem Texto

*    EntradaSaida    →   (input Identificador) | (print Identificador)

*    Atribuicao      →   let Identificador = ExpressaoAritmetica

*    Goto            →   goto NumeroLinha

*    Condicional     →   if ExpressaoRelacional goto NumeroLinha

*    Finaliza        →   end
*/

/*
[ Árvore de recursividade do Parser ]

* expr →        equality()

* equality →    comparison( "==" | "!=" comparison )*

* comparison →  term ((">" | ">=" | "<" | "<=") term )*

* term →        factor (("+" | "-") factor )*

* factor →      primary (("/" | "*") primary )*

* primary →     (NUMERIC_LITERAL | VARIAVEL | INPUT)
*/

/*
Originalmente é:
    expression     → literal
               | unary
               | binary
               | grouping ;

    literal        → NUMBER | STRING | "true" | "false" | "nil" ;
    grouping       → "(" expression ")" ;
    unary          → ( "-" | "!" ) expression ;
    binary         → expression operator expression ;
    operator       → "==" | "!=" | "<" | "<=" | ">" | ">="
                    | "+"  | "-"  | "*" | "/" ;
*/

class Parser {
    private static class ParseError extends RuntimeException {}
    private final List<Token> tokens;
    private int current = 0;

    Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    private Expr expression() {
        return equality();
    }

    private Expr equality() {
        Expr expr = comparison();

        while (match(DIFERENTE_DE, IGUAL_A)) {
            Token operator = previous();
            Expr right = comparison();
            expr = new Expr.Binary(expr, operator, right);
        }

        return expr;
    }

    private Expr comparison() {
        Expr expr = term();

        while (match(MAIOR, MAIOR_IGUAL, MENOR, MENOR_IGUAL)) {
            Token operator = previous();
            Expr right = term();
            expr = new Expr.Binary(expr, operator, right);
        }

        return expr;
    }

    private Expr term() {
        Expr expr = factor();

        while (match(SUBTRACAO, ADICAO)) {
            Token operator = previous();
            Expr right = factor();
            expr = new Expr.Binary(expr, operator, right);
        }

        return expr;
    }

    private Expr factor() {
        Expr expr = primary();

        while (match(DIVISAO, MULTIPLICACAO)) {
            Token operator = previous();
            Expr right = primary();
            expr = new Expr.Binary(expr, operator, right);
        }

        return expr;
    }

    private Expr primary() {
        while (match(NOVA_LINHA) || match(FIM_DE_TEXTO)) {}

        if (match(CONSTANTE_NUMERICA, VARIAVEL, COMENTARIO)) {
            return new Expr.Literal(previous().conteudo);
        }

        if (peek().tipo == END) {
            return null;
        }

        throw error(peek(), "Falta uma expressão.");

    }

    private boolean match(TokenTipo... tipos) {
        for (TokenTipo tipo : tipos) {
            if (check(tipo)) {
                advance();
                return true;
            }
        }

        return false;
    }

    private Token consume(TokenTipo tipo, String message) {
        if (check(tipo)) return advance();

        throw error(peek(), message);
    }

    private ParseError error(Token token, String message) {
        Simple.error(token, message);
        return new ParseError();
    }

    private void synchronize() {
        advance();

        while (!isAtEnd()) {
            if (previous().tipo == FIM_DE_TEXTO) return;

            switch (peek().tipo) {
                case COMENTARIO:
                case INPUT:
                case LET:
                case PRINT:
                case VARIAVEL:
                case IF:
                case GOTO:
                    return;
            }

            advance();
        }
    }

    private boolean check(TokenTipo type) {
        if (isAtEnd()) return false;
        return peek().tipo == type;
    }

    private Token advance() {
        if (!isAtEnd()) current++;
        return previous();
    }

    private boolean isAtEnd() {
        return peek().tipo == END;
    }

    private Token peek() {
        return tokens.get(current);
    }

    private Token previous() {
        return tokens.get(current - 1);
    }

    Expr parse() {
        Token ultimoToken = tokens.get(tokens.size() - 1);

        //if (ultimoToken.tipo != END) {
        //    throw error(ultimoToken, "O programa deve acabar após a instrução END.");
        //}

        Expr expr = null;
        try {
            // Parsing should handle multiple expressions if needed
            while (!isAtEnd()) {
                expr = expression(); // Parse each expression
                // You may want to do something with 'expr' here, such as adding it to a list of expressions.
            }
        } catch (ParseError error) {
            synchronize();
        }

        return expr; // Return the last parsed expression
    }
}
