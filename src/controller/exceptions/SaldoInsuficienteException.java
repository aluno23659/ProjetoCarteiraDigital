package exceptions;

/**
 * Exceção personalizada para quando o utilizador tenta levantar 
 * ou transferir mais fundos do que os que tem disponíveis.
 */
public class SaldoInsuficienteException extends Exception {
    
    public SaldoInsuficienteException(String mensagem) {
        super(mensagem);
    }
}