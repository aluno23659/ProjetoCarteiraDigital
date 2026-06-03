
package model.wallet;

import java.util.Objects;


public abstract class Wallet {

    protected String adress;

    public abstract boolean requireValidation();

    public Wallet(String adress) {
        this.adress = adress;
    }

    public String getAddress() {
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

    private static final long serialVersionUID = 202605261730L;


}
