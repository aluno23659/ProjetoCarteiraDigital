
package model.wallet;

public class SystemWallet extends Wallet {

    public SystemWallet() {
        super("System");
    }

    @Override
    public boolean requireValidation() {
        return false;
    }

    private static final long serialVersionUID = 202605261733L;


}
