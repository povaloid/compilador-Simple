package simple;

import javax.rmi.ssl.SslRMIClientSocketFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.MarshalledObject;
import java.util.ArrayList;
import java.util.List;

public class Simple {
    static boolean contemErro = false;

    public static void main(String[] args) throws IOException {
        if (args.length > 1) {
            System.out.println("Para rodar, insira o codigo de teste no arquivo programa.txt");
            System.exit(64);
        } else if (args.length == 1) {
            runFile(args[0]);
        } else {
            runPrompt();
        }
    }
    private static void runFile(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        run(new String(bytes, Charset.defaultCharset()));
    }
    private static void runPrompt() throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        for (;;) {
            System.out.print("> ");
            String line = reader.readLine();
            if (line == null) break;
            run(line);
            contemErro = false;
        }
    }
    private static void run(String source) {
        Scanner scanner = new Scanner(source);
        Interpreter interpretador = new Interpreter();

        // Registra o percurso do scanner e separa por linha
        List<Token> tokens = scanner.scanTokens();
        List<List<Token>> linhasDeInstrucao = scanner.formalizarLinhasDeInstrucao(tokens);

        // Inicializa a lista de Declarações
        List<Declaracao> arvoresDeInstrucao = new ArrayList<Declaracao>();
        List<Token> tokensEmPosOrdem = new ArrayList<>();

        String programa = "---Programa Simple---";

        // Define a ordenação de execução de cada linha na lista
        for (List<Token> linha: linhasDeInstrucao) {
            Declaracao declaracao = new Declaracao(linha);
            arvoresDeInstrucao.add(declaracao);

            // Estrutura a linha em Pós-Ordem
            tokensEmPosOrdem = declaracao.arvore.postOrderTraversal();

            programa += "\n"+interpretador.traduzirLinha(tokensEmPosOrdem);
        }

        System.out.println(programa);
        System.out.println("Memoria: "+interpretador.memoria);

        System.exit(0);
    }
    static void error(int line, String message) {
        report(line, "", message);
    }

    private static void report(int line, String where,
                               String message) {
        System.err.println(
                "[line " + line + "] Error" + where + ": " + message);
        contemErro = true;
    }
    static void error(Token token, String message) {
        if (token.tipo == TokenTipo.END) {
            report(token.linha, " at end", message);
        }
        else if (token.tipo == TokenTipo.FIM_DE_TEXTO ) {
            report(token.linha, " at ' " + "'", message);
        }
        else {
            report(token.linha, " at '" + token.lexema + "'", message);
        }
    }
}
