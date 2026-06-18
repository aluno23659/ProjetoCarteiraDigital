package model.data;

import model.transactions.Transaction;
import model.wallet.Wallet;
import model.coin.CoinFactory; // Importamos a nossa fábrica
import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Gestor do livro de registos (Ledger) persistido em formato de ficheiro de texto CSV.
 * <p>
 * Esta classe é responsável por implementar os métodos de gravação em tempo real e de 
 * leitura de segurança (backup) do histórico de transações. Utiliza a {@link CoinFactory} 
 * para reconstruir os objetos de moeda corretos a partir das strings lidas do ficheiro.
 * </p>
 *
 * @author Sidnei e Jose
 * @version 2.0
 * @see model.data.Ledger
 */
public class CsvLedger extends Ledger {

    /** O caminho de diretório ou nome do ficheiro físico armazenado no disco (ex: "dados.csv"). */
    private final String filePath;
    
    /** Lista em memória que atua como a cache de transações carregadas do sistema. */
    private ArrayList<Transaction> data;

    /**
     * Construtor do Ledger CSV. Inicializa as estruturas de dados e dispara a leitura do ficheiro.
     * <p>
     * Caso o ficheiro definido no caminho já exista, o método carrega automaticamente 
     * todo o histórico antigo para a memória.
     * </p>
     *
     * @param filePath O caminho absoluto ou relativo para o ficheiro CSV de dados.
     */
    public CsvLedger(String filePath) {
        this.filePath = filePath;
        this.data = new ArrayList<>();
        carregarTransacoes();
    }

    /**
     * Adiciona uma nova transação à lista em memória e força a sua escrita imediata no ficheiro.
     *
     * @param t A instância da {@link Transaction} a ser registada e persistida.
     */
    @Override
    public void add(Transaction t) {
        data.add(t);
        gravarTransacao(t);
    }

    /**
     * Filtra e devolve todas as transações em que uma determinada carteira participou, 
     * seja como conta de origem ou como conta de destino.
     *
     * @param w A instância da carteira ({@link Wallet}) utilizada como critério de filtro.
     * @return  Uma lista ({@link ArrayList}) contendo apenas as transações vinculadas à carteira informada.
     */
    @Override
    public ArrayList<Transaction> filter(Wallet w) {
        ArrayList<Transaction> userData = new ArrayList<>();
        for (Transaction transaction : data) {
            if (transaction.getSource().equals(w) || transaction.getDestination().equals(w)) {
                userData.add(transaction);
            }
        }
        return userData;
    }

    /**
     * Método auxiliar privado que anexa uma única transação ao fim do ficheiro CSV.
     * <p>
     * O formato da linha gravada obedece à seguinte ordem delimitada por pontos e vírgulas:
     * {@code EnderecoOrigem;EnderecoDestino;RepresentacaoMoeda;ValorQuantidade;TimestampMilissegundos}
     * </p>
     *
     * @param t A transação cujos dados primitivos serão convertidos e gravados em texto puro.
     */
    private void gravarTransacao(Transaction t) {
        try (FileWriter writer = new FileWriter(this.filePath, true)) {

            // CORREÇÃO: Agora guardamos também o nome da moeda como a 3ª coluna!
            String linhaCSV = t.getSource().getAddress() + ";" +
                    t.getDestination().getAddress() + ";" +
                    t.getCoin().toString() + ";" +   // <--- Grava a moeda real (BTC, ETH, EUR...)
                    t.getAmount() + ";" +
                    System.currentTimeMillis() + "\n";

            writer.write(linhaCSV);

        } catch (IOException e) {
            System.out.println("ERRO CRÍTICO: Não foi possível gravar a transação no ficheiro.");
            System.out.println(e.getMessage());
        }
    }

    /**
     * Efetua a leitura sequencial do ficheiro CSV, interpreta as linhas de texto, 
     * reconstrói as entidades de domínio do sistema e popula a lista em memória.
     * <p>
     * Faz uso da limpeza posicional de índices (Splitting) e delega à {@link CoinFactory} 
     * o papel de transformar a string guardada no índice {@code [2]} num objeto tipado de moeda.
     * </p>
     */
    private void carregarTransacoes() {
        java.io.File ficheiro = new java.io.File(this.filePath);

        if (!ficheiro.exists()) {
            return;
        }

        try (java.util.Scanner scanner = new java.util.Scanner(ficheiro)) {
            while (scanner.hasNextLine()) {
                String linha = scanner.nextLine();
                String[] partes = linha.split(";"); // [Origem, Destino, Moeda, Valor, Data]

                // CORREÇÃO: Agora garantimos que a linha tem pelo menos 4 elementos
                if (partes.length >= 4) {
                    String origemStr = partes[0];
                    String destinoStr = partes[1];
                    String moedaStr = partes[2];   // <--- Lemos o texto da moeda
                    double valor = Double.parseDouble(partes[3]); // <--- O valor passou para o índice 3

                    Wallet origem = new model.wallet.RegularWallet(origemStr);
                    Wallet destino = new model.wallet.RegularWallet(destinoStr);

                    // CORREÇÃO: Usamos a Fábrica para obter o objeto da moeda correto!
                    model.coin.Currency moedaObj = CoinFactory.criarMoeda(moedaStr);

                    // Criamos a transação com a moeda dinâmica recuperada do CSV
                    Transaction t = new Transaction(origem, destino, moedaObj, valor);

                    this.data.add(t);
                }
            }
            System.out.println("BACKUP: Foram carregadas " + this.data.size() + " transações do ficheiro!");
        } catch (Exception e) {
            System.out.println("Erro ao carregar o backup: " + e.getMessage());
        }
    }

    /**
     * Devolve a lista completa e em tempo real de todas as transações carregadas em memória.
     *
     * @return O {@link ArrayList} contendo o acervo total de objetos {@link Transaction}.
     */
    public java.util.ArrayList<model.transactions.Transaction> getElements() {
        return this.data;
    }
}