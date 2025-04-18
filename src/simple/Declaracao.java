package simple;

import java.util.ArrayList;
import java.util.List;

public class Declaracao {

    final int linha;
    Tree<Token> arvore;

    Declaracao(List<Token> conteudo){
        this.linha = conteudo.get(0).linha;
        this.definirArvore(conteudo);
    }

    private void definirArvore(List<Token> conteudo) {
        if (arvore == null)
                arvore = new Tree<Token>();

        for (int i = 0; i < conteudo.size(); i++){
            arvore.insertNode(conteudo.get(i));
        }
    }

    // Define a estrutura da Arvore
    class Tree<T extends Comparable<T>> {
        public TreeNode<T> root = null;

        public Tree() {
            root = null;
        }

        public void insertNode(T insertValue) {
            if (root == null)
                root = new TreeNode<T>(insertValue); // Define a raiz
            else
                root.insert(insertValue);
        }

        public List<T> postOrderTraversal() {
            List<T> tokensEmPosOrdem = new ArrayList<>();
            postOrderHelper(root, tokensEmPosOrdem);
            return tokensEmPosOrdem;
        }

        private void postOrderHelper(TreeNode<T> node, List<T> tkns) {

            if (node == null)
                return;

            postOrderHelper(node.esquerda, tkns); // Percorre a subarvore esquerda
            postOrderHelper(node.direita, tkns); // Percorre a subarvore direita

            tkns.add(node.data);
        }
    } // Fim da classe Arvore

    // Define a classe dos Nós da Árvore
    class TreeNode<T extends Comparable<T>>{
        TreeNode<T> esquerda;
        T data;
        TreeNode<T> direita;

        public TreeNode(T nodeData) {
            data = nodeData;
            esquerda = direita = null; // inicialmente nao possui filhos
        }

        // Localiza o ponto de inserção e insere o novo nó; Ignora os valores duplicados
        public void insert(T insertValue){
            // Insere na subarvore esquerda
            if (insertValue.compareTo(data) < 0) {
                // Insere novo TreeNode
                if (esquerda == null) {
                    esquerda = new TreeNode<>(insertValue);
                } else {
                    esquerda.insert(insertValue);
                }
            }
            // Insere na subarvore direita
            else if (insertValue.compareTo(data) > 0) {
                if (direita == null) {
                    direita = new TreeNode<>(insertValue);
                } else {
                    direita.insert(insertValue);
                }
            }
        }
    } // Fim da classe TreeNode

    public String toString() {
        return this.linha + " | Raiz: " + this.arvore.root.data + "\n";
    }
}

