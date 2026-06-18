package model.coin;

/**
 * Representa a moeda fiduciária Dólar no sistema da carteira.
 * <p>
 * Esta classe herda as características base da classe abstrata {@link Currency}.
 * Permite a definição dinâmica do seu nome e símbolo no momento da instanciação, 
 * o que a torna útil para suportar diferentes variações da moeda (como Dólar Americano USD, 
 * Dólar Canadiano CAD, entre outros).
 * </p>
 *
 * @author Seu Nome ou Organização
 * @version 1.0
 * @see model.coin.Currency
 */
public class Dolar extends Currency {

    /**
     * Construtor que inicializa a moeda Dólar com um nome e símbolo específicos.
     * <p>
     * Invoca o construtor da superclasse ({@code super}) para registar as strings 
     * identificadoras que serão usadas nas operações de cálculo e na interface gráfica.
     * </p>
     *
     * @param name   O nome específico da variação do Dólar (ex: "Dólar Americano").
     * @param symbol O símbolo ou código internacional correspondente (ex: "USD", "$").
     */
    public Dolar(String name, String symbol) {
        super(name, symbol);
    }
}