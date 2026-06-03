package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class NovaTransacaoView extends JDialog {

    private JTextField txtOrigem;
    private JTextField txtDestino;
    private JTextField txtValor;
    private JButton btnGravar;

    public NovaTransacaoView(JFrame parent) {
        super(parent, "Nova Transação", true);
        setSize(380, 250);
        setLocationRelativeTo(parent);

        // 1. O Painel Principal (Aplica o grande segredo profissional: Margens!)
        JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
        painelPrincipal.setBorder(new EmptyBorder(20, 20, 20, 20)); // 20 píxeis de "ar" à volta

        // 2. O Painel do Formulário (A grelha agora só controla os textos)
        JPanel painelForm = new JPanel(new GridLayout(3, 2, 10, 15));

        painelForm.add(new JLabel("Carteira Origem:"));
        txtOrigem = new JTextField();
        painelForm.add(txtOrigem);

        painelForm.add(new JLabel("Carteira Destino:"));
        txtDestino = new JTextField();
        painelForm.add(txtDestino);

        painelForm.add(new JLabel("Valor (em BTC):"));
        txtValor = new JTextField();
        painelForm.add(txtValor);

        // 3. O Painel do Botão (FlowLayout mantém o tamanho normal do botão e encosta-o à direita)
        JPanel painelBotao = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnGravar = new JButton("Gravar Transação");
        painelBotao.add(btnGravar);

        // 4. Montar o Lego (Juntar os painéis todos na janela)
        painelPrincipal.add(painelForm, BorderLayout.CENTER);
        painelPrincipal.add(painelBotao, BorderLayout.SOUTH);
        add(painelPrincipal);
    }

    // Getters mantêm-se inalterados para o Controller funcionar
    public String getOrigem() { return txtOrigem.getText(); }
    public String getDestino() { return txtDestino.getText(); }
    public String getValor() { return txtValor.getText(); }
    public JButton getBtnGravar() { return btnGravar; }
}