package model.coin;

public class CoinFactory {

    // Método estático: não precisas de instanciar a fábrica para a usar
    public static Currency criarMoeda(String nomeMoeda) {

        // Limpeza de segurança (tira espaços vazios e mete tudo em maiúsculas para evitar erros)
        String nomeLimpo = nomeMoeda.replace("(true)", "").replace("(false)", "").trim().toUpperCase();

        if (nomeLimpo.contains("ETH") || nomeLimpo.contains("ETHEREUM")) {
            return new Ether();
        } else if (nomeLimpo.contains("EUR") || nomeLimpo.contains("EURO")) {
            return new Euro();
        } else {
            // Se não reconhecer, ou se for BTC, devolve Bitcoin por defeito
            return new BitCoin();
        }
    }
}