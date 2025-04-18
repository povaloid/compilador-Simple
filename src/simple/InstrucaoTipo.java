package simple;

public enum InstrucaoTipo {
    // Operações de entrada/saída:
        READ (10),	        // Lê uma palavra do teclado para uma posição específica da memória.
        WRITE (11),	        // Escreve na tela uma palavra de uma posição específica da memória.

    //Operações de carga/armazenamento:
        LOAD (20),          //	Carrega uma palavra de uma posição específica na memória para o acumulador.
        STORE (21),	        //  Armazena uma palavra do acumulador em uma posição específica na memória.

    // Operações aritméticas:
        ADD (30),           //	Adiciona uma palavra de uma posição específica na memória à palavra no acumulador (deixa o resultado no acumulador).
        SUBTRACT (31),      //  Subtrai uma palavra de uma posição específica na memória à palavra no acumulador (deixa o resultado no acumulador).
        DIVIDE (32),        //	Divide a palavra que está no acumulador por uma palavra de uma posição específica na memória (deixa o resultado no acumulador).
        MULTIPLY (33),      //	Multiplica uma palavra de uma posição específica na memória pela palavra no acumulador (deixa o resultado no acumulador).
        MODULE (34),        //	Resto da divisão da palavra de uma posição específica na memória pela palavra no acumulador (deixa o resultado no acumulador).

    // Operações de transferência de controle:
        BRANCH (40),        //	Desvia para uma posição específica na memória.
        BRANCHNEG (41),     //	Desvia para uma posição específica na memória se o acumulador for negativo.
        BRANCHZERO (42),    //	Desvia para uma posição específica na memória se o acumulador for zero.
        HALT (43);	        //  Finaliza o programa.

    private final int uid;
    private InstrucaoTipo(final int uid) { this.uid = uid; }

    public int getUid(){ return uid; }
}
