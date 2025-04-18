package simple;

public class Token implements Comparable<Token> {
    final TokenTipo tipo;
    final String lexema;
    Object conteudo;
    final int linha;

    Token(TokenTipo tipo, String lexema, Object conteudo, int linha) {
        this.tipo = tipo;
        this.lexema = lexema;
        this.conteudo = conteudo;
        this.linha = linha;
    }

    public TokenTipo getTipo() { return tipo; }
    public String getLexema() { return lexema; }
    public Object getConteudo() { return conteudo; }
    public int getLinha() { return linha; }


    public String toString() {
        return "[" + getLinha() + ", " + getTipo().getUid() + ", " + getLexema() + ", " + getConteudo() + "]";
    }

    @Override
    public int compareTo(Token token) {
        if (this == token)
            return 0;
        return 1;
    }
}
