package model.data;

import model.transactions.Transaction;
import model.wallet.Wallet;

import java.util.ArrayList;

public class MemoryLedger  extends Ledger{
    ArrayList<Transaction> data = new ArrayList<>();
    @Override
    public void add(Transaction t) {
        data.add(t);
    }


    @Override
    public ArrayList<Transaction> filter(Wallet w) {
        ArrayList<Transaction> userData = new ArrayList<>();
        for (Transaction transaction : data) {
            if( transaction.getSource().equals(w) ||
                    transaction.getDestination().equals(w))
                userData.add(transaction);
        }
        return userData;

    }

    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2026  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////
}
