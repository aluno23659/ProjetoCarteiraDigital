package model.coin;

/**
 * Representa a moeda fiduciária oficial Euro (EUR) no sistema da carteira.
 * <p>
 * Esta classe herda as características base da classe abstrata {@link Currency},
 * definindo de forma fixa o nome ("Euro") e o símbolo/ticker internacional ("EUR") 
 * que servem como par de referência principal para a consolidação e exibição de 
 * saldos totais na interface gráfica.
 * </p>
 *
 * @author Sidnei e Jose
 * @version 1.0
 * @see model.coin.Currency
 */
public class Euro extends Currency {

    /**
     * Identificador de versão único para o processo de serialização.
     * <p>
     * Essencial para garantir a integridade dos dados e a compatibilidade do estado do 
     * objeto caso os históricos de transações em Euros sejam gravados ou lidos de fluxos binários.
     * </p>
     */
    private static final long serialVersionUID = 202605261752L;

    /**
     * Construtor padrão que inicializa a moeda Euro.
     * <p>
     * Invoca o construtor da superclasse ({@code super}) para registar formalmente 
     * o nome legível "Euro" e a sigla de mercado "EUR".
     * </p>
     */
    public Euro() {
        // Chama o construtor da classe mãe (Currency) passando o Nome e o Símbolo
        super("Euro", "EUR");
    }
}