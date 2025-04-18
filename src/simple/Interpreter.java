package simple;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Interpreter {

    private int posicaoInicialNaMemoria;
    private int acumulador;
    public Map<Integer, String> memoria;

    Interpreter() {
        this.acumulador = 0;
        this.memoria = new HashMap<Integer, String>();
        this.posicaoInicialNaMemoria = 0;
    }

    public String traduzirLinha(List<Token> contexto) {
        String instrucoes = "";
        for (Token tkn: contexto){
            instrucoes = traduzir(tkn, contexto) + instrucoes;
        }

        if (contemOperacao(contexto) != "U") {
            instrucoes = "L"+contexto.get(0).linha + " " + refatorarOpBinaria(instrucoes,
                                                                contemOperacao(contexto));
        }

        return instrucoes;
    }


    private String traduzir(Token tkn, List<Token> contexto) {
        int indice = contexto.indexOf(tkn);
        switch (tkn.tipo) {
            case IF:
            case ATRIBUICAO: return "+" + InstrucaoTipo.LOAD.getUid();

            case MENOR:
            case MAIOR: // Subtrai do valor no acumulador
                return "+" + InstrucaoTipo.SUBTRACT.getUid();

            case GOTO:
                if (contemOperacao(contexto) != "U"){
                    return traduzGotoBinario(contexto);
                } else {
                    return "+" + InstrucaoTipo.BRANCH.getUid();
                }

            case MULTIPLICACAO: return "+" + InstrucaoTipo.MULTIPLY.getUid();

            case RESTO_DIVISAO: return "+" + InstrucaoTipo.MODULE.getUid();

            case DIVISAO: return "+" + InstrucaoTipo.DIVIDE.getUid();

            case SUBTRACAO: return "+" + InstrucaoTipo.SUBTRACT.getUid();

            case ADICAO: return "+" + InstrucaoTipo.ADD.getUid();

            case END: return "+" + InstrucaoTipo.HALT.getUid() + "00";

            case PRINT: return "+" + InstrucaoTipo.WRITE.getUid();

            case FIM_DE_TEXTO: return "\n";

            case INPUT: return "+" + InstrucaoTipo.READ.getUid();

            // Tratar as constantes numericas e linhas

            case CONSTANTE_NUMERICA:
                return traduzirConstante(tkn, contexto, indice);

            case VARIAVEL:
                return traduzirVariavel(tkn, contexto, indice);

            case NOVA_LINHA:
                return "L"+ tkn.lexema + " ";

            case LET:

            default: return "";
        }
    }

    // Caso 1: operacao unaria
    // Caso 2: operacao binaria
    // Caso 3: operacao condicional
    String contemOperacao(List<Token> linha) {
        // Preciso melhorar a logica desta funcao
        for (Token t: linha) {
            if (t.tipo == TokenTipo.LET) { return "B"; }
            else if (t.tipo == TokenTipo.IF) { return "C"; }
        }
        return "U";
    }

    String traduzirConstante(Token tkn,List<Token> contexto, int indice) {

        if (contexto.get(indice + 1).tipo == TokenTipo.GOTO) {
            return "(L"+tkn.lexema+")";
        }

        if (!memoria.containsValue(tkn.lexema)) {
            memoria.put(posicaoInicialNaMemoria + 1, tkn.lexema);
            posicaoInicialNaMemoria++;
        }

        return "("+tkn.lexema+")";
    }

    String traduzirVariavel(Token tkn,List<Token> contexto, int indice) {
        if (!memoria.containsValue(tkn.lexema)) {
            memoria.put(posicaoInicialNaMemoria + 1, tkn.lexema);
            posicaoInicialNaMemoria++;
        }

        if (contexto.get(indice + 2).tipo == TokenTipo.NOVA_LINHA
                && contemOperacao(contexto) == "B") {
            return "+"+InstrucaoTipo.STORE.getUid()+"("+tkn.lexema+")";
        }

        return "("+tkn.lexema+")";
    }

    String traduzGotoBinario(List<Token> contexto) {
        // Implementar baseado na organizacao de memoria
        for (Token t: contexto) {
            switch (t.tipo) {
                case MAIOR:
                    return "+" + InstrucaoTipo.BRANCHZERO.getUid();
                case MAIOR_IGUAL:
                case MENOR:
                    return "+" + InstrucaoTipo.BRANCHNEG.getUid();
                case MENOR_IGUAL:
                case IGUAL_A:
                case DIFERENTE_DE:
            }
        }

        return "+-";
    }

    String refatorarOpBinaria(String linha, String tipo){
        // Usar regex para capturar cada bloco que começa com + e termina com )
        Pattern pattern = Pattern.compile("\\+[^)]+\\)");
        Matcher matcher = pattern.matcher(linha);

        // Array para armazenar os três blocos encontrados
        String[] blocks = new String[3];
        int i = 0;

        while (matcher.find() && i < 3) {
            blocks[i] = matcher.group();
            i++;
        }

        String resultado = "";

        if (tipo == "C") {
            // Retorna igual, mas com quebra de linha
            resultado = blocks[0] + "\n" + blocks[1] + "\n" + blocks[2];
        } else if (tipo == "B") {
            if (i == 3) {
                // Se houver 3 blocos, concatene como bloco2 + bloco3 + bloco1
                resultado = blocks[1] + "\n" + blocks[2] + "\n" + blocks[0];
            } else if (i == 2) {
                // Se houver 2 blocos, concatene como bloco1 + bloco2
                resultado = blocks[1] + "\n" + blocks[0];
            }
        }

        return resultado;
    }
}