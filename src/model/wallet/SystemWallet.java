package model.wallet;

/**
 * Representa a carteira digital interna do próprio Sistema.
 * <p>
 * Esta classe estende a superclasse abstrata {@link Wallet}, atuando como a conta central 
 * automatizada do ecossistema. Por ser uma entidade fidedigna e controlada pelo núcleo 
 * do programa (geralmente associada a operações de recompensa, taxas ou inicialização), 
 * as suas transações dispensam qualquer processo de validação externa.
 * </p>
 *
 * @author Sidnei e Jose
 * @version 1.0
 * @see model.wallet.Wallet
 */
public class SystemWallet extends Wallet {

    /**
     * Identificador de versão único para o processo de serialização.
     * <p>
     * Garante a consistência e a integridade estrutural dos dados ao guardar ou restaurar 
     * o estado desta carteira central em ficheiros binários ou fluxos de persistência.
     * </p>
     */
    private static final long serialVersionUID = 202605261733L;

    /**
     * Construtor padrão que inicializa a carteira do sistema.
     * <p>
     * Invoca o construtor da superclasse ({@code super}) definindo de forma fixa 
     * o nome ou endereço identificador como "System".
     * </p>
     */
    public SystemWallet() {
        super("System");
    }

    /**
     * Define se as operações originadas por esta carteira necessitam de validação.
     * <p>
     * Sobrepõe o comportamento abstrato da classe mãe para ditar que, por se tratar da 
     * carteira core do sistema ({@code SystemWallet}), todos os movimentos de saída são 
     * considerados seguros por predefinição.
     * </p>
     *
     * @return {@code false} de forma fixa, indicando que a validação não é necessária.
     */
    @Override
    public boolean requireValidation() {
        return false;
    }
}