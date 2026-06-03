package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class NovaTransacaoView extends JDialog {

    private JTextField txtOrigem;
    private JTextField txtDestino;
    private JComboBox<String> cbMoeda; // Novo componente!
    private JTextField txtValor;
    private JButton btnGravar;

    public NovaTransacaoView(JFrame parent) {
        super(parent, "Nova Transação", true);
        setSize(380, 280); // Aumentei um pouco a altura para caber a nova linha
        setLocationRelativeTo(parent);

        JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
        painelPrincipal.setBorder(new EmptyBorder(20, 20, 20, 20));

        // A grelha agora tem 4 linhas em vez de 3
        JPanel painelForm = new JPanel(new GridLayout(4, 2, 10, 15));

        painelForm.add(new JLabel("Carteira Origem:"));
        txtOrigem = new JTextField();
        painelForm.add(txtOrigem);

        painelForm.add(new JLabel("Carteira Destino:"));
        txtDestino = new JTextField();
        painelForm.add(txtDestino);

        // NOVA LINHA: A caixa de seleção das moedas
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

    public String getOrigem() { return txtOrigem.getText(); }
    public String getDestino() { return txtDestino.getText(); }
    public String getValor() { return txtValor.getText(); }

    // Novo getter para saber qual foi a moeda escolhida na caixa
    public String getMoedaSelecionada() { return (String) cbMoeda.getSelectedItem(); }

    public JButton getBtnGravar() { return btnGravar; }
}