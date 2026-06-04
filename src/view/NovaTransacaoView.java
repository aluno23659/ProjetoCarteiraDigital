package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class NovaTransacaoView extends JDialog {

    private JComboBox<String> cbOrigem;  // Agora é um menu pendente!
    private JComboBox<String> cbDestino; // Agora é um menu pendente!
    private JComboBox<String> cbMoeda;
    private JTextField txtValor;
    private JButton btnGravar;

    // O construtor agora recebe a lista de carteiras que existem no sistema
    public NovaTransacaoView(JFrame parent, List<String> carteirasExistentes) {
        super(parent, "Nova Transação", true);
        setSize(380, 280);
        setLocationRelativeTo(parent);

        JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
        painelPrincipal.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel painelForm = new JPanel(new GridLayout(4, 2, 10, 15));

        // Transformar a lista do Java num formato que a ComboBox entenda
        String[] arrayCarteiras = carteirasExistentes.toArray(new String[0]);

        painelForm.add(new JLabel("Carteira Origem:"));
        cbOrigem = new JComboBox<>(arrayCarteiras);
        painelForm.add(cbOrigem);

        painelForm.add(new JLabel("Carteira Destino:"));
        cbDestino = new JComboBox<>(arrayCarteiras);
        painelForm.add(cbDestino);

        painelForm.add(new JLabel("Moeda:"));
        String[] opcoesMoedas = {"Bitcoin (BTC)", "Ethereum (ETH)", "Euro (EUR)"};
        cbMoeda = new JComboBox<>(opcoesMoedas);
        painelForm.add(cbMoeda);

        painelForm.add(new JLabel("Valor:"));
        txtValor = new JTextField();
        painelForm.add(txtValor);

        JPanel painelBotao = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnGravar = new JButton("Gravar Transação");
        painelBotao.add(btnGravar);

        painelPrincipal.add(painelForm, BorderLayout.CENTER);
        painelPrincipal.add(painelBotao, BorderLayout.SOUTH);
        add(painelPrincipal);
    }

    // Os novos Getters para os menus pendentes
    public String getOrigemSelecionada() { return (String) cbOrigem.getSelectedItem(); }
    public String getDestinoSelecionado() { return (String) cbDestino.getSelectedItem(); }
    public String getMoedaSelecionada() { return (String) cbMoeda.getSelectedItem(); }
    public String getValor() { return txtValor.getText(); }
    public JButton getBtnGravar() { return btnGravar; }
}