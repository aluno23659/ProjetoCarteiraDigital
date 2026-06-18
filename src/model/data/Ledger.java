package model.data;

import model.wallet.Wallet;
import model.transactions.Transaction;
import java.util.ArrayList;

/**
 * Classe abstrata que define o contrato fundamental para o Livro de Registos (Ledger).
 * <p>
 * Serve como base para qualquer mecanismo de persistência ou armazenamento de histórico 
 * do sistema (como bases de dados, ficheiros binários ou o {@link CsvLedger}). Garante 
 * que todos os gestores de dados possuam métodos para registar e filtrar transações.
 * </p>
 *
 * @author Seu Nome ou Organização
 * @version 1.0
 */
public abstract class Ledger {

    /**
     * Identificador de versão único para o processo de serialização.
     * <p>
     * Assegura a consistência e compatibilidade dos dados caso a estrutura 
     * das classes derivadas do Ledger seja transmitida ou guardada em disco.
     * </p>
     */
    private static final long serialVersionUID = 202605261806L;

    /**
     * Adiciona e persiste uma nova transação no livro de registos.
     * <p>
     * As subclasses devem implementar a lógica específica para guardar a transação, 
     * seja atualizando uma lista em memória, escrevendo num ficheiro ou inserindo 
     * num banco de dados.
     * </p>
     *
     * @param t A instância da {@link Transaction} contendo os dados de origem, destino, 
     * moeda e valor a ser registada.
     */
    public abstract void add(Transaction t);

    /**
     * Filtra e recupera o histórico de transações associadas a uma carteira específica.
     * <p>
     * A implementação concreta deve percorrer os registos e retornar todas as transações 
     * onde a carteira fornecida atue como origem (remetente) ou destino (destinatário).
     * </p>
     *
     * @param w A carteira ({@link Wallet}) utilizada como critério de busca.
     * @return  Um {@link ArrayList} contendo todas as transações correspondentes ao filtro.
     */
    public abstract ArrayList<Transaction> filter(Wallet w);
}