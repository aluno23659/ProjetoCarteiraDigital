package test;

import model.coin.BitCoin;
import model.coin.Currency;
import model.coin.Ether;

/**
 * Classe de teste responsável por verificar a correta inicialização e o 
 * comportamento polimórfico das subclasses de {@link Currency}.
 * <p>
 * Este ambiente de execução simples (ponto de entrada {@code main}) serve para validar 
 * se os nomes e símbolos de mercado do {@link BitCoin} e do {@link Ether} estão a ser 
 * montados e formatados corretamente através do método {@code toString()}.
 * </p>
 *
 * @author Sidnei e Jose
 * @version 1.0
 */
public class TestCurrency {

    /**
     * Identificador de versão único para o processo de serialização.
     * <p>
     * Nota: Embora classes de teste com métodos estáticos raramente sejam serializadas, 
     * o atributo mantém a consistência da assinatura de boas práticas do projeto.
     * </p>
     */
    private static final long serialVersionUID = 202605261753L;

    /**
     * Ponto de entrada principal da aplicação de teste.
     * <p>
     * Instancia os objetos das moedas utilizando referências da superclasse abstrata 
     * e imprime o resultado no fluxo de saída padrão (consola) para auditoria visual.
     * </p>
     *
     * @param args Argumentos da linha de comandos passados na execução (não utilizados).
     */
    public static void main(String[] args) {

        // Instanciação e teste da criptomoeda Bitcoin
        Currency c3 = new BitCoin();
        System.out.println("c3 = " + c3);
        
        // Instanciação e teste da criptomoeda Ether
        Currency c4 = new Ether();
        System.out.println("c4 = " + c4);
    }
}