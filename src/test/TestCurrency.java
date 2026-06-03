
package test;

import model.coin.BitCoin;
import model.coin.Currency;
import model.coin.Ether;

public class TestCurrency {


    public static void main(String[] args) {

        Currency c3 = new BitCoin();
        System.out.println("c1 = " + c3);
        Currency c4 = new Ether();
        System.out.println("c4 = " + c4);

    }

    private static final long serialVersionUID = 202605261753L;
}
