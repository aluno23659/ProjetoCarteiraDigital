//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
//::                                                                         ::
//::     Antonio Manuel Rodrigues Manso                                      ::
//::                                                                         ::
//::     I N S T I T U T O   P O L I T E C N I C O   D E   T O M A R         ::
//::     Escola Superior de Tecnologia de Tomar                              ::
//::     e-mail: manso@ipt.pt                                                ::
//::     url   : http://orion.ipt.pt/~manso                                  ::
//::                                                                         ::
//::     This software was build with the purpose of investigate and         ::
//::     learning.                                                           ::
//::                                                                         ::
//::                                                                  (c)2026 ::
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
//////////////////////////////////////////////////////////////////////////////

package model.transactions;

import model.coin.Currency;
import model.wallet.Wallet;

/**
 * Representa uma transação financeira de transferência de fundos no sistema.
 * <p>
 * Esta classe encapsula os dados relativos ao movimento de ativos, contendo a referência 
 * da carteira de origem, da carteira de destino, o tipo de moeda transacionada e o montante 
 * correspondente. É a entidade base para o registo no livro de atas (Ledger).
 * </p>
 *
 * @author Antonio Manuel Rodrigues Manso
 * @version 1.0
 */
public class Transaction {

    /** A carteira ({@link Wallet}) remetente que envia os fundos. */
    protected Wallet source;

    /** A carteira ({@link Wallet}) destinatária que recebe os fundos. */
    protected Wallet destination;

    /** A unidade monetária ou criptomoeda ({@link Currency}) utilizada na operação. */
    protected Currency coin;

    /** O volume ou quantidade numérica de fundos movimentada na transação. */
    protected double amount;

    /**
     * Identificador de versão único para o processo de serialização.
     * <p>
     * Garante a consistência estrutural dos dados ao persistir ou ler históricos 
     * de transações em fluxos de bytes.
     * </p>
     */
    private static final long serialVersionUID = 202605261801L;

    /**
     * Construtor completo para instanciar e parametrizar uma nova transação.
     *
     * @param source      A carteira de origem dos fundos.
     * @param destination A carteira de destino dos fundos.
     * @param coin        O tipo de moeda em que a operação é efetuada.
     * @param amount      O valor quantitativo a ser transferido.
     */
    public Transaction(Wallet source, Wallet destination, Currency coin, double amount) {
        this.source = source;
        this.destination = destination;
        this.coin = coin;
        this.amount = amount;
    }

    /**
     * Devolve a carteira de origem (remetente) da transação.
     *
     * @return A instância da {@link Wallet} de origem.
     */
    public Wallet getSource() {
        return source;
    }

    /**
     * Devolve a carteira de destino (destinatário) da transação.
     *
     * @return A instância da {@link Wallet} de destino.
     */
    public Wallet getDestination() {
        return destination;
    }

    /**
     * Devolve a moeda associada ao registo desta transação.
     *
     * @return O objeto {@link Currency} correspondente.
     */
    public Currency getCoin() {
        return coin;
    }

    /**
     * Devolve o montante financeiro movimentado nesta transação.
     *
     * @return O valor numérico (double) da transferência.
     */
    public double getAmount() {
        return amount;
    }
}