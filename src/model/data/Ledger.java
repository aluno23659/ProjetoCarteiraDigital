package model.data;

import model.wallet.Wallet;
import model.transactions.Transaction;
import java.util.ArrayList;

public abstract class Ledger {

    public abstract void add(Transaction t);

    public abstract ArrayList<Transaction> filter(Wallet w);



    private static final long serialVersionUID = 202605261806L;
}
