//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
//::                                                                         ::
//::     Antonio Manuel Rodrigues Manso                                      ::
//::                                                                         ::
//::     I N S T I T U T O   P O L I T E C N I C O   D E   T O M A R         ::
//::     Escola Superior de Tecnologia de Tomar                              ::
//::     e-mail: manso@ipt.pt                                                ::
//::     url   : http://orion.ipt.pt/~manso                                  ::
//::                                                                         ::
//::     This software was build with the purpose of investigate and         ::
//::     learning.                                                           ::
//::                                                                         ::
//::                                                                  (c)2026 ::
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
//////////////////////////////////////////////////////////////////////////////

package model.wallet;

/**
 * Representa uma carteira digital do tipo Regular no sistema.
 * <p>
 * Esta classe estende a superclasse abstrata {@link Wallet}, definindo uma carteira 
 * padrão que, por motivos de segurança e integridade do protocolo, exige obrigatoriamente 
 * a validação ou assinatura das suas transações antes de serem processadas.
 * </p>
 *
 * @author Antonio Manuel Rodrigues Manso
 * @version 1.0
 * @see model.wallet.Wallet
 */
public class RegularWallet extends Wallet {

    /**
     * Identificador de versão único para o processo de serialização.
     * <p>
     * Assegura a compatibilidade e a consistência dos dados ao guardar ou recuperar 
     * o estado desta carteira em ficheiros ou fluxos de persistência.
     * </p>
     */
    private static final long serialVersionUID = 202605261734L;

    /**
     * Construtor que inicializa a carteira regular atribuindo-lhe um identificador ou nome.
     * <p>
     * Encaminha o parâmetro recebido para o construtor da superclasse através do comando {@code super(name)}.
     * </p>
     *
     * @param name O nome, endereço ou identificador único a ser associado a esta carteira.
     */
    public RegularWallet(String name) {
        super(name);
    }

    /**
     * Define se as operações originadas por esta carteira necessitam de validação.
     * <p>
     * Sobrapõe o comportamento abstrato da classe mãe para ditar que, para a {@code RegularWallet}, 
     * todas as transações de saída dependem obrigatoriamente de uma verificação ativa de segurança.
     * </p>
     *
     * @return {@code true} de forma fixa, indicando que a validação é estritamente necessária.
     */
    @Override
    public boolean requireValidation() {
        return true;
    }
}