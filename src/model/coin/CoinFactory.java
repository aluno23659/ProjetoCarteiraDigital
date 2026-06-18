package model.coin;

/**
 * Fábrica responsável pela instanciação dinâmica de moedas (Padrão de Desenho Factory).
 * <p>
 * Esta classe centraliza a lógica de criação dos objetos que herdam de {@link Currency}.
 * Sendo uma fábrica estática, elimina a necessidade de instanciar a classe {@code CoinFactory} 
 * para obter uma nova moeda.
 * </p>
 *
 * @author Sidnei e Jose
 * @version 1.0
 */
public class CoinFactory {

    /**
     * Instancia e devolve o objeto de moeda correspondente com base numa string de identificação.
     * <p>
     * O método aplica uma limpeza de segurança na string recebida (removendo marcadores de estado, 
     * espaços em branco e convertendo o texto para maiúsculas) para garantir que variações na escrita 
     * não quebrem a lógica de correspondência.
     * </p>
     *
     * @param nomeMoeda O nome ou sigla da moeda enviada pela interface gráfica ou modelo (ex: "Euro (EUR)", "BTC").
     * @return          Uma instância de {@link Ether} se contiver ETH/ETHEREUM, 
     * uma instância de {@link Euro} se contiver EUR/EURO, 
     * ou uma instância de {@link BitCoin} como moeda padrão (defeito).
     */
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