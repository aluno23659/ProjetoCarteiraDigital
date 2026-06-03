package model.coin;

public class Euro extends Currency {

    // Um identificador único de serialização (boa prática, apenas incrementei o último dígito)
    private static final long serialVersionUID = 202605261752L;

    public Euro() {
        // Chama o construtor da classe mãe (Currency) passando o Nome e o Símbolo
        super("Euro", "EUR");
    }
}