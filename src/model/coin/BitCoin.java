package model.coin;

/**
 * Representa a criptomoeda Bitcoin (BTC) no sistema da carteira.
 * <p>
 * Esta classe herda as características base da classe abstrata {@link Currency},
 * definindo de forma fixa o nome ("BitCoin") e o símbolo/ticker ("BTC") 
 * que serão utilizados nas operações de balanço, histórico e câmbio.
 * </p>
 *
 * @author Sidnei e Jose
 * @version 1.0
 * @see model.coin.Currency
 */
public class BitCoin extends Currency {
    
    /** * Identificador de versão único para o processo de serialização.
     * <p>
     * Garante a compatibilidade e a integridade dos dados ao guardar ou ler
     * objetos desta classe em ficheiros binários ou transmissões de dados.
     * </p>
     */
    private static final long serialVersionUID = 202605261751L;

    /**
     * Construtor padrão que inicializa a moeda Bitcoin.
     * <p>
     * Invoca o construtor da classe base ({@code super}) para registar formalmente
     * o nome legível "BitCoin" e a sigla de mercado "BTC".
     * </p>
     */
    public BitCoin() {
        super("BitCoin", "BTC");
    }

}