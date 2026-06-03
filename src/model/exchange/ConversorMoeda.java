package model.exchange;

import model.coin.Currency;
import java.util.HashMap;

/**
 * Classe responsável por gerir taxas de câmbio e converter valores.
 * Esta é uma funcionalidade extra para valorização do projeto.
 */
public class ConversorMoeda {

    // Um "dicionário" que guarda as taxas de câmbio usando o Euro como base (1 EUR = X)
    private static final HashMap<String, Double> taxas = new HashMap<>();

    // Este bloco estático corre automaticamente quando o programa arranca
    static {
        // Valores fictícios de mercado.
        // A chave (String) tem de ser igual ao nome que deste às moedas!
        taxas.put("BitCoin", 60000.0);  // 1 BTC = 60000 EUR
        taxas.put("Ether", 3000.0);     // 1 ETH = 3000 EUR
        taxas.put("Euro", 1.0);         // 1 EUR = 1 EUR
        taxas.put("Dolar", 0.92);       // 1 USD = 0.92 EUR
    }

    /**
     * Converte qualquer quantidade de qualquer moeda para Euros.
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
     * Converte entre quaisquer duas moedas (Ex: BTC para USD)
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