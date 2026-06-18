package exceptions;

/**
 * Exceção personalizada para indicar que uma operação financeira falhou devido a fundos insuficientes.
 * <p>
 * Esta exceção é disparada na camada do Controller sempre que o utilizador tenta realizar 
 * um levantamento ou uma transferência cujo valor ultrapassa o saldo líquido disponível 
 * na carteira ativa para a moeda selecionada.
 * </p>
 * * @author Seu Nome ou Organização
 * @version 1.0
 */
public class SaldoInsuficienteException extends Exception {
    
    /**
     * Construtor que define a mensagem detalhada do erro de saldo.
     * <p>
     * Esta mensagem será capturada pelo bloco {@code catch} no controlador e exibida 
     * diretamente ao utilizador através de uma janela de alerta (JOptionPane).
     * </p>
     *
     * @param mensagem O texto descritivo detalhando o saldo em falta e o atual da conta.
     */
    public SaldoInsuficienteException(String mensagem) {
        super(mensagem);
    }
}