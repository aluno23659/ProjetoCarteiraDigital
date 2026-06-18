package test;

import model.wallet.RegularWallet;
import model.wallet.SystemWallet;
import model.wallet.Wallet;

/**
 * Classe de teste responsável por verificar o comportamento, a representation textual 
 * e os critérios de igualdade das subclasses de {@link Wallet}.
 * <p>
 * Este ambiente de execução isolado (ponto de entrada {@code main}) serve para validar 
 * o polimorfismo do método {@code requireValidation()} refletido no {@code toString()}, 
 * bem como a sobreposição do método {@code equals()} ao comparar duas instâncias diferentes 
 * de carteiras que partilham o mesmo endereço alfanumérico.
 * </p>
 *
 * @author Sidnei e Jose
 * @version 1.0
 */
public class TestWallet {

    /**
     * Identificador de versão único para o processo de serialização.
     * <p>
     * Mantém o alinhamento de boas práticas e a assinatura de compatibilidade padronizada 
     * em todo o ecossistema do projeto.
     * </p>
     */
    private static final long serialVersionUID = 202605261736L;

    /**
     * Ponto de entrada principal da aplicação de teste para Carteiras.
     * <p>
     * O fluxo deste método executa as seguintes validações:
     * </p>
     * <ol>
     * <li>Instancia uma carteira regular para a utilizadora "ana" ({@code w1}).</li>
     * <li>Instancia a carteira central automatizada do sistema ({@code s2}).</li>
     * <li>Instancia uma segunda carteira regular, independente na memória, também para "ana" ({@code w2}).</li>
     * <li>Imprime a representação textual das carteiras para auditar o sufixo de validação {@code (true/false)}.</li>
     * <li>Testa o método {@link Wallet#equals(Object)} para garantir que {@code w1} e {@code w2} são consideradas iguais por possuírem o mesmo endereço.</li>
     * </ol>
     *
     * @param args Argumentos da linha de comandos passados na execução (não utilizados).
     */
    public static void main(String[] args) {
        // Inicialização das carteiras de teste
        Wallet w1 = new RegularWallet("ana");
        Wallet s2 = new SystemWallet();
        Wallet w2 = new RegularWallet("ana");

        // Exibição das carteiras e o respetivo estado de validação
        System.out.println(w1);
        System.out.println(s2);
        
        // Verificação do critério de igualdade baseado no endereço
        System.out.println("w1 == w2 ? " + w1.equals(w2));
    }
}