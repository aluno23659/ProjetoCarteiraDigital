package model.coin;

/**
 * Representa a criptomoeda Ether (ETH) da rede Ethereum no sistema da carteira.
 * <p>
 * Esta classe herda as características base da classe abstrata {@link Currency},
 * definindo de forma estática o nome ("Ether") e o símbolo/ticker de mercado ("ETH") 
 * que serão processados nas rotinas de cálculo de saldo, histórico e conversões.
 * </p>
 *
 * @author Seu Nome ou Organização
 * @version 1.0
 * @see model.coin.Currency
 */
public class Ether extends Currency {
    
    /**
     * Identificador de versão único para o processo de serialização.
     * <p>
     * Garante que o estado dos objetos desta classe permaneça compatível e possa 
     * ser recuperado sem erros estruturais ao ler ou gravar dados em ficheiros.
     * </p>
     */
    private static final long serialVersionUID = 202605261751L;

    /**
     * Construtor padrão que inicializa a moeda Ether.
     * <p>
     * Invoca o construtor da superclasse ({@code super}) para registar o nome 
     * legível "Ether" e a sigla internacional "ETH".
     * </p>
     */
    public Ether() {
        super("Ether", "ETH");
    }

}