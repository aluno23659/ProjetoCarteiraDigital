
package test;

import java.util.ArrayList;
import model.coin.BitCoin;
import model.coin.Currency;
import model.data.Ledger;
import model.data.MemoryLedger;
import model.transactions.Transaction;
import model.wallet.RegularWallet;
import model.wallet.SystemWallet;
import model.wallet.Wallet;

public class TestLedger {

    public static void main(String[] args) {
        Ledger mySystem = new MemoryLedger();

        Wallet s1 = new SystemWallet();
        Wallet r2 = new RegularWallet("ana");
        Wallet r3 = new RegularWallet("maria");
        Wallet r4= new RegularWallet("joao");
        Currency bitcoin = new BitCoin();


        mySystem.add(new Transaction(s1, r2, bitcoin, 100));
        mySystem.add(new Transaction(s1, r3, bitcoin, 100));
        //invalido
        mySystem.add(new Transaction(r4, r3, bitcoin, 10));

        ArrayList<Transaction> maria = mySystem.filter(r3);
        for (Transaction transaction : maria) {
            System.out.println(transaction);
        }

    }



    private static final long serialVersionUID = 202605261818L;
}
