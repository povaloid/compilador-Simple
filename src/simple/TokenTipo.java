package simple;

public enum TokenTipo {

    // Delimitadores
    NOVA_LINHA (10), FIM_DE_TEXTO (03),

    // Operadores
    ATRIBUICAO (11),

    // Operadores aritmeticos
    ADICAO (21), SUBTRACAO(22), MULTIPLICACAO(23), DIVISAO(24), RESTO_DIVISAO(25),

    // Operadores relacionais.
    IGUAL_A (31), DIFERENTE_DE(32),
    MAIOR(33), MAIOR_IGUAL(34),
    MENOR(34), MENOR_IGUAL(36),

    // Identificadores.
    VARIAVEL(41), CONSTANTE_NUMERICA(51),

    // Palavras reservadas
    COMENTARIO(61), INPUT(62), LET(63), PRINT(64), GOTO(65), IF(66), END(67);

    private int uid;
    private TokenTipo(final int uid) { this.uid = uid; }

    public int getUid(){ return uid; }
    }
