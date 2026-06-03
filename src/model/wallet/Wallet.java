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

package model.wallet;

import java.util.Objects;


public abstract class Wallet {

    protected String adress;

    public abstract boolean requireValidation();

    public Wallet(String adress) {
        this.adress = adress;
    }

    public String getAdress() {
        return adress;
    }

    @Override
    public String toString(){
        return adress + "(" + requireValidation() + ")";
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        //conversao de object para Wallet
        final Wallet other = (Wallet) obj;
        //comparar os endereços
        return this.adress.equals(other.adress);
    }








    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 202605261730L;


}
