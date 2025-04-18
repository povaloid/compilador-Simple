package simple;

public class Instrucao {
    final InstrucaoTipo tipo;
    final Object conteudo;

    Instrucao(InstrucaoTipo tipo, Object conteudo) {
        this.tipo = tipo;
        this.conteudo = conteudo;
    }

    public InstrucaoTipo getTipo() { return tipo; }
    public Object getConteudo() { return conteudo; }

    public String toString() {
        return "[ " + getTipo() + ": " + getConteudo() + "]";
    }
}
