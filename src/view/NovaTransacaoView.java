package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

/**
 * Interface gráfica secundária do tipo diálogo modal para o registo de uma Nova Transação.
 * <p>
 * Esta classe herda de {@link JDialog} e disponibiliza um formulário estruturado para a 
 * criação de transferências. Para mitigar erros de digitação e aumentar a segurança, as 
 * carteiras de origem e destino são dinamicamente selecionadas através de menus pendentes 
 * ({@link JComboBox}) alimentados pelo estado atual do sistema.
 * </p>
 *
 * @author Seu Nome ou Organização
 * @version 2.0
 * @see javax.swing.JDialog
 */
public class NovaTransacaoView extends JDialog {

    /** Caixa de seleção (Dropdown) para definir a carteira remetente (origem) dos fundos. */
    private JComboBox<String> cbOrigem;  
    
    /** Caixa de seleção (Dropdown) para definir a carteira destinatária (destino) dos fundos. */
    private JComboBox<String> cbDestino; 
    
    /** Caixa de seleção (Dropdown) para o isolamento do ativo monetário a ser operado. */
    private JComboBox<String> cbMoeda;
    
    /** Campo de texto destinado à inserção quantitativa do valor a transferir. */
    private JTextField txtValor;
    
    /** Botão de ação para submeter, validar e gravar o registo da transação. */
    private JButton btnGravar;

    /**
     * Construtor da janela de diálogo NovaTransacaoView.
     * <p>
     * Inicializa a janela em modo modal (travando interações externas) e converte a coleção 
     * dinâmica de carteiras registadas num vetor primitivo aceitável pelas instâncias de JComboBox.
     * </p>
     *
     * @param parent              A janela principal ({@link JFrame}) à qual este diálogo se vincula.
     * @param carteirasExistentes Lista contendo os endereços ou nomes de todas as carteiras em memória.
     */
    public NovaTransacaoView(JFrame parent, List<String> carteirasExistentes) {
        super(parent, "Nova Transação", true);
        setSize(380, 280);
        setLocationRelativeTo(parent);

        JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
        painelPrincipal.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Define uma matriz tabular de 4 linhas por 2 colunas para o formulário
        JPanel painelForm = new JPanel(new GridLayout(4, 2, 10, 15));

        // Transformar a lista do Java num formato que a ComboBox entenda (String[])
        String[] arrayCarteiras = carteirasExistentes.toArray(new String[0]);

        // Configuração da linha 1: Seleção de Origem
        painelForm.add(new JLabel("Carteira Origem:"));
        cbOrigem = new JComboBox<>(arrayCarteiras);
        painelForm.add(cbOrigem);

        // Configuração da linha 2: Seleção de Destino
        painelForm.add(new JLabel("Carteira Destino:"));
        cbDestino = new JComboBox<>(arrayCarteiras);
        painelForm.add(cbDestino);

        // Configuração da linha 3: Seleção do Ativo Financiar
        painelForm.add(new JLabel("Moeda:"));
        String[] opcoesMoedas = {"Bitcoin (BTC)", "Ethereum (ETH)", "Euro (EUR)"};
        cbMoeda = new JComboBox<>(opcoesMoedas);
        painelForm.add(cbMoeda);

        // Configuração da linha 4: Input do Montante
        painelForm.add(new JLabel("Valor:"));
        txtValor = new JTextField();
        painelForm.add(txtValor);

        // Painel inferior para acomodar o botão de submissão alinhado à direita
        JPanel painelBotao = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnGravar = new JButton("Gravar Transação");
        painelBotao.add(btnGravar);

        // Agrupamento dos painéis de acordo com o posicionamento geográfico do BorderLayout
        painelPrincipal.add(painelForm, BorderLayout.CENTER);
        painelPrincipal.add(painelBotao, BorderLayout.SOUTH);
        add(painelPrincipal);
    }

    /**
     * Recupera o item textual atualmente selecionado no menu pendente de origem.
     *
     * @return O endereço ou nome da carteira remetente em formato String.
     */
    public String getOrigemSelecionada() { 
        return (String) cbOrigem.getSelectedItem(); 
    }

    /**
     * Recupera o item textual atualmente selecionado no menu pendente de destino.
     *
     * @return O endereço ou nome da carteira destinatária em formato String.
     */
    public String getDestinoSelecionado() { 
        return (String) cbDestino.getSelectedItem(); 
    }

    /**
     * Recupera a moeda ou token selecionado para a transferência.
     *
     * @return A designação textual selecionada (ex: "Bitcoin (BTC)").
     */
    public String getMoedaSelecionada() { 
        return (String) cbMoeda.getSelectedItem(); 
    }

    /**
     * Obtém o conteúdo digitado no campo de montante monetário.
     * <p>
     * <b>Nota de Controlo:</b> Lembre-se de tratar a potencial exceção 
     * {@code NumberFormatException} no controlador ao efetuar o parsing do texto para número.
     * </p>
     *S
     * @return A String contendo o valor do campo de input.
     */
    public String getValor() { 
        return txtValor.getText(); 
    }

    /**
     * Devolve o botão de submissão do formulário.
     *
     * @return O componente {@link JButton} associado ao evento de gravação.
     */
    public JButton getBtnGravar() { 
        return btnGravar; 
    }
}