
package test;

import model.coin.BitCoin;
import model.coin.Currency;
import model.transactions.Transaction;
import model.wallet.RegularWallet;
import model.wallet.SystemWallet;
import model.wallet.Wallet;

public class TestTransaction {


    public static void main(String[] args) {
        Wallet s = new SystemWallet();
        Wallet d = new RegularWallet("Ana");
        Currency c = new BitCoin();

        Transaction t = new Transaction(s, d, c, 100);

    }

    private static final long serialVersionUID = 202605261803L;
}
