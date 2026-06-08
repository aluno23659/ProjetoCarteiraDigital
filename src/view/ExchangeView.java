package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ExchangeView extends JDialog {
    private JComboBox<String> cbMoedaOrigem;
    private JComboBox<String> cbMoedaDestino;
    private JTextField txtQuantidade;
    private JButton btnConfirmar;

    public ExchangeView(JFrame parent) {
        super(parent, "Crypto Swap (Exchange Interno)", true);
        setSize(350, 250);
        setLocationRelativeTo(parent);

        JPanel p = new JPanel(new GridLayout(4, 2, 10, 15));
        p.setBorder(new EmptyBorder(20, 20, 20, 20));

        p.add(new JLabel("Vender (De):"));
        cbMoedaOrigem = new JComboBox<>(new String[]{"Bitcoin (BTC)", "Ethereum (ETH)", "Euro (EUR)"});
        p.add(cbMoedaOrigem);

        p.add(new JLabel("Comprar (Para):"));
        cbMoedaDestino = new JComboBox<>(new String[]{"Ethereum (ETH)", "Bitcoin (BTC)", "Euro (EUR)"});
        p.add(cbMoedaDestino);

        p.add(new JLabel("Quantidade:"));
        txtQuantidade = new JTextField();
        p.add(txtQuantidade);

        p.add(new JLabel(""));
        btnConfirmar = new JButton("Confirmar Troca");
        p.add(btnConfirmar);

        add(p);
    }

    public String getMoedaOrigem() { return (String) cbMoedaOrigem.getSelectedItem(); }
    public String getMoedaDestino() { return (String) cbMoedaDestino.getSelectedItem(); }
    public String getQuantidade() { return txtQuantidade.getText(); }
    public JButton getBtnConfirmar() { return btnConfirmar; }
}