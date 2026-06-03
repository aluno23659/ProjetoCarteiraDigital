
package test;

import model.wallet.RegularWallet;
import model.wallet.SystemWallet;
import model.wallet.Wallet;

public class TestWallet {

    public static void main(String[] args) {
        Wallet w1 = new RegularWallet("ana");
        Wallet s2 = new SystemWallet();
        Wallet w2 = new RegularWallet("ana");

        System.out.println(w1);
        System.out.println(s2);
        System.out.println("w1 == w2 ? " +  w1.equals(w2));
    }

    private static final long serialVersionUID = 202605261736L;

}
