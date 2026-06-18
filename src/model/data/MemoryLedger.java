package model.data;

import model.transactions.Transaction;
import model.wallet.Wallet;
import java.util.ArrayList;

/**
 * Implementação concreta do livro de registos (Ledger) em memória RAM.
 * <p>
 * Esta classe armazena o histórico de transações de forma volátil utilizando um {@link ArrayList}. 
 * Os dados são perdidos assim que a aplicação é encerrada, tornando esta classe ideal para 
 * ambientes de testes unitários, simulações ou como uma cache temporária de alta performance.
 * </p>
 *
 * @author Sidnei e Jose
 * @version 1.0
 * @see model.data.Ledger
 */
public class MemoryLedger extends Ledger {

    /** Lista interna que atua como repositório em memória para as transações registadas. */
    ArrayList<Transaction> data = new ArrayList<>();

    /**
     * Regista uma nova transação adicionando-a diretamente à lista em memória.
     * <p>
     * Por se tratar de um armazenamento em memória, esta operação é instantânea, 
     * mas não efetua qualquer persistência em disco ou ficheiro externo.
     * </p>
     *
     * @param t A instância da {@link Transaction} a ser adicionada ao histórico.
     */
    @Override
    public void add(Transaction t) {
        data.add(t);
    }

    /**
     * Filtra e recupera todas as transações em memória associadas a uma determinada carteira.
     * <p>
     * Percorre a lista local e seleciona os registos onde a carteira informada 
     * figura como origem (remetente) ou como destino (destinatário).
     * </p>
     *
     * @param w A carteira ({@link Wallet}) utilizada como critério de filtragem.
     * @return  Um {@link ArrayList} contendo as transações correspondentes encontradas na memória.
     */
    @Override
    public ArrayList<Transaction> filter(Wallet w) {
        ArrayList<Transaction> userData = new ArrayList<>();
        for (Transaction transaction : data) {
            if (transaction.getSource().equals(w) ||
                    transaction.getDestination().equals(w)) {
                userData.add(transaction);
            }
        }
        return userData;
    }
}