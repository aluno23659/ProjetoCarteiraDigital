package model.exchange;

import model.coin.Currency;
import java.util.HashMap;

/**
 * Classe utilitária responsável por gerir as taxas de câmbio e converter valores entre moedas.
 * <p>
 * Esta classe utiliza o Euro (EUR) como moeda base (pivô) para centralizar e simplificar as 
 * equações de conversão. Trata-se de uma funcionalidade avançada desenvolvida para adicionar 
 * valorização técnica ao projeto de gestão da carteira.
 * </p>
 *
 * @author Seu Nome ou Organização
 * @version 1.0
 */
public class ConversorMoeda {

    /**
     * Dicionário dinâmico (Map) que mapeia o nome de cada moeda à sua respetiva taxa de conversão em Euros.
     * <p>
     * O rácio obedece à lógica: {@code 1 unidade da Moeda X = Y Euros}.
     * </p>
     */
    private static final HashMap<String, Double> taxas = new HashMap<>();

    // Bloco estático executado automaticamente aquando do carregamento da classe na JVM
    static {
        // Valores fictícios de mercado.
        // A chave (String) tem de ser igual ao nome definido nas classes filhas de Currency!
        taxas.put("BitCoin", 60000.0);  // 1 BTC = 60000 EUR
        taxas.put("Ether", 3000.0);     // 1 ETH = 3000 EUR
        taxas.put("Euro", 1.0);         // 1 EUR = 1 EUR
        taxas.put("Dolar", 0.92);       // 1 USD = 0.92 EUR
    }

    /**
     * Converte uma determinada quantidade de uma moeda qualquer para o seu valor equivalente em Euros.
     * <p>
     * <b>Nota de Manutenção:</b> Certifique-se de que o método {@code moeda.getName()} condiz 
     * com a assinatura exata definida na sua superclasse {@link Currency}.
     * </p>
     *
     * @param moeda      A instância da moeda de origem ({@link Currency}) que se pretende avaliar.
     * @param quantidade O montante numérico a ser convertido.
     * @return           O valor correspondente em Euros (double). Se a moeda não for reconhecida, retorna {@code 0.0}.
     */
    public static double paraEuro(Currency moeda, double quantidade) {
        // ATENÇÃO: Substitui o getName() pelo método correto do teu professor
        // que devolve o nome da moeda (ex: getNome(), getDesignation(), etc.)
        String nomeMoeda = moeda.getName();

        // Vai buscar a taxa ao dicionário. Se a moeda não existir, assume taxa 0.
        double taxa = taxas.getOrDefault(nomeMoeda, 0.0);

        return quantidade * taxa;
    }

    /**
     * Realiza a conversão direta de valores entre duas moedas genéricas (ex: BTC para USD).
     * <p>
     * O algoritmo divide-se em duas etapas consecutivas:
     * <ol>
     * <li>Normaliza o montante de origem transformando-o em Euros através do método {@link #paraEuro(Currency, double)}.</li>
     * <li>Divide o subtotal obtido em Euros pela taxa de mercado da moeda de destino.</li>
     * </ol>
     * </p>
     *
     * @param moedaOrigem  A moeda base atual do saldo (remetente).
     * @param moedaDestino A moeda para a qual o valor será transposto (destinatário).
     * @param quantidade   O volume de fundos a flutuar no câmbio.
     * @return             O resultado líquido da conversão na unidade monetária de destino.
     */
    public static double converter(Currency moedaOrigem, Currency moedaDestino, double quantidade) {
        // Passo 1: Converter a origem para a base (Euros)
        double valorEmEuros = paraEuro(moedaOrigem, quantidade);

        // Passo 2: Converter dos Euros para o destino
        // ATENÇÃO: Novamente, substitui o getName()
        String nomeDestino = moedaDestino.getName();
        double taxaDestino = taxas.getOrDefault(nomeDestino, 1.0);

        return valorEmEuros / taxaDestino;
    }
}