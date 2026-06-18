package test;

import model.coin.BitCoin;
import model.coin.Currency;
import model.transactions.Transaction;
import model.wallet.RegularWallet;
import model.wallet.SystemWallet;
import model.wallet.Wallet;

/**
 * Classe de teste responsável por verificar a correta instanciação e estruturação 
 * de objetos da classe {@link Transaction}.
 * <p>
 * Este ambiente de execução isolado (ponto de entrada {@code main}) serve para validar 
 * se a associação de dependências entre as carteiras (origem e destino), o tipo de moeda 
 * e o montante numérico ocorre sem falhas estruturais ou exceções de ponteiro nulo (NullPointerException).
 * </p>
 *
 * @author Seu Nome ou Organização
 * @version 1.0
 */
public class TestTransaction {

    /**
     * Identificador de versão único para o processo de serialização.
     * <p>
     * Mantém o alinhamento de boas práticas e a assinatura de compatibilidade padronizada 
     * em todo o ecossistema do projeto.
     * </p>
     */
    private static final long serialVersionUID = 202605261803L;

    /**
     * Ponto de entrada principal da aplicação de teste para Transações.
     * <p>
     * O fluxo deste método executa as seguintes etapas:
     * </p>
     * <ol>
     * <li>Instancia uma carteira fidedigna de origem ({@link SystemWallet}).</li>
     * <li>Instancia uma carteira nominal de destino ({@link RegularWallet} para a utilizadora "Ana").</li>
     * <li>Define o ativo transacionável como sendo a criptomoeda {@link BitCoin}.</li>
     * <li>Agrupa estas entidades num único registo de {@link Transaction} com o valor de 100 unidades.</li>
     * </ol>
     *
     * @param args Argumentos da linha de comandos passados na execução (não utilizados).
     */
    public static void main(String[] args) {
        // Inicialização das carteiras envolvidas no teste
        Wallet s = new SystemWallet();
        Wallet d = new RegularWallet("Ana");
        
        // Definição da moeda de teste
        Currency c = new BitCoin();

        // Instanciação e validação estrutural da transação
        Transaction t = new Transaction(s, d, c, 100);
    }
}