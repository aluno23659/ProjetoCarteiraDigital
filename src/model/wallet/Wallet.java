package model.wallet;

import java.util.Objects;

/**
 * Classe abstrata que estabelece a estrutura e o comportamento base de uma Carteira (Wallet).
 * <p>
 * Serve como base para os diferentes perfis de carteiras do sistema, garantindo que todas 
 * possuam um endereço identificador único, capacidade de auto-representação textual, 
 * regras de igualdade posicional e um contrato polimórfico de validação de segurança.
 * </p>
 *
 * @author Seu Nome ou Organização
 * @version 1.0
 */
public abstract class Wallet {

    /** O endereço alfanumérico ou identificador único da carteira (ex: "System", "0x71C..."). */
    protected String adress;

    /**
     * Identificador de versão único para o processo de serialização.
     * <p>
     * Assegura a compatibilidade dos dados ao guardar ou recuperar estruturas 
     * derivadas de carteiras em ficheiros de persistência binária.
     * </p>
     */
    private static final long serialVersionUID = 202605261730L;

    /**
     * Método abstrato que dita se as transações de saída desta carteira exigem validação ativa.
     * <p>
     * Cada subclasse concreta deve estipular a sua própria regra de negócio (ex: as carteiras 
     * regulares requerem validação obrigatória, enquanto as do sistema são fidedignas).
     * </p>
     *
     * @return {@code true} se a validação for necessária, {@code false} caso contrário.
     */
    public abstract boolean requireValidation();

    /**
     * Construtor protegido para inicializar uma carteira com o seu respetivo endereço.
     * <p>
     * Por se tratar de uma classe abstrata, este construtor é invocado estritamente pelas 
     * subclasses através do mecanismo {@code super(adress)}.
     * </p>
     *
     * @param adress O endereço identificador único a ser atribuído à carteira.
     */
    public Wallet(String adress) {
        this.adress = adress;
    }

    /**
     * Devolve o endereço identificador da carteira.
     *
     * @return O endereço alfanumérico (String).
     */
    public String getAddress() {
        return adress;
    }

    /**
     * Gera uma representação textual personalizada do objeto da carteira.
     * <p>
     * Junta o endereço e o estado de validação num formato legível. É frequentemente 
     * utilizado para preencher elementos visuais da interface gráfica e logs.
     * </p>
     *
     * @return Uma String formatada no padrão "endereco(true/false)".
     */
    @Override
    public String toString(){
        return adress + "(" + requireValidation() + ")";
    }

    /**
     * Compara esta carteira com outro objeto para verificar a sua igualdade jurídica.
     * <p>
     * Duas carteiras são consideradas estruturalmente iguais se pertencerem exatamente à mesma 
     * classe e se os seus endereços alfanuméricos forem equivalentes (case-sensitive).
     * </p>
     *
     * @param obj O objeto a ser comparado com esta instância.
     * @return    {@code true} se os objetos forem semanticamente idênticos; {@code false} caso contrário.
     */
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
        // Conversão de object para Wallet
        final Wallet other = (Wallet) obj;
        // Comparar os endereços
        return this.adress.equals(other.adress);
    }
}