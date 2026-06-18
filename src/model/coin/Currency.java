package model.coin;

/**
 * Classe abstrata que define a estrutura base para qualquer moeda no sistema.
 * <p>
 * Serve como superclasse para todas as moedas reais e criptomoedas (como {@link BitCoin}, 
 * {@code Ether} e {@code Euro}), garantindo que todas possuam, obrigatoriamente, 
 * um nome legível e um símbolo identificador (ticker de mercado).
 * </p>
 *
 * @author Sidnei e Jose
 * @version 1.0
 */
public abstract class Currency {

    /** O nome descritivo e por extenso da moeda (ex: "BitCoin", "Euro"). */
    protected final String name;
    
    /** O símbolo ou ticker de mercado que representa a moeda (ex: "BTC", "EUR"). */
    protected final String symbol;

    /**
     * Construtor protegido para inicializar os atributos fundamentais da moeda.
     * <p>
     * Por pertencer a uma classe abstrata, este construtor não pode ser invocado diretamente, 
     * sendo obrigatoriamente chamado pelas subclasses através do comando {@code super(name, symbol)}.
     * </p>
     *
     * @param name   O nome por extenso que será atribuído à moeda.
     * @param symbol A sigla/ticker oficial que representará a moeda no mercado.
     */
    public Currency(String name, String symbol) {
        this.name = name;
        this.symbol = symbol;
    }

    /**
     * Devolve o nome por extenso da moeda.
     *
     * @return O nome da moeda (String).
     */
    public String getName() {
        return name;
    }

    /**
     * Devolve o símbolo ou ticker de mercado que identifica a moeda.
     *
     * @return O símbolo da moeda (String).
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * Gera uma representação textual customizada do objeto da moeda.
     * <p>
     * Este método sobrepõe o comportamento padrão do Java para concatenar o nome 
     * e o símbolo num formato padronizado. É amplamente utilizado pelas tabelas 
     * da interface gráfica e logs de auditoria.
     * </p>
     *
     * @return Uma String formatada no padrão "Nome(Símbolo)" (ex: "BitCoin(BTC)").
     */
    public String toString(){
        return name + "(" + symbol + ")";
    }

}