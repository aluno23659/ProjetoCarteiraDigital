//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
//::                                                                         ::
//::     Antonio Manuel Rodrigues Manso                                      ::
//::                                                                         ::
//::     I N S T I T U T O    P O L I T E C N I C O   D E   T O M A R        ::
//::     Escola Superior de Tecnologia de Tomar                              ::
//::     e-mail: manso@ipt.pt                                                ::
//::     url   : http://orion.ipt.pt/~manso                                  ::
//::                                                                         ::
//::     This software was build with the purpose of investigate and         ::
//::     learning.                                                           ::
//::                                                                         ::
//::                                                               (c)2026   ::
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
//////////////////////////////////////////////////////////////////////////////

package test;

import model.wallet.RegularWallet;
import model.wallet.SystemWallet;
import model.wallet.Wallet;

/**
 * Created on 26/05/2026, 17:36:58
 *
 * @author manso - computer
 */
public class TestWallet {

    public static void main(String[] args) {
        Wallet w1 = new RegularWallet("ana");
        Wallet s2 = new SystemWallet();
        Wallet w2 = new RegularWallet("ana");

        System.out.println(w1);
        System.out.println(s2);
        System.out.println("w1 == w2 ? " +  w1.equals(w2));
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 202605261736L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2026  :::::::::::::::::::

///////////////////////////////////////////////////////////////////////////
}
