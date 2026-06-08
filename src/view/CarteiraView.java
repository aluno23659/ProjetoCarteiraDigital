package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CarteiraView extends JFrame {

    private JLabel lblSaldoGeral;
    private JComboBox<String> cbCarteiraAtiva;
    private JButton btnNovaTransacao;
    private JButton btnVerHistorico;
    private JButton btnNovaCarteira;
    private JButton btnExchange;
    private JButton btnDeposito;

    private JTable tabelaCarteiras;
    private DefaultTableModel modeloTabela;

    public CarteiraView() {
        setTitle("Crypto Wallet Pro v2.0");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 550);
        setLocationRelativeTo(null);

        JPanel contentPane = new JPanel(new BorderLayout(15, 15));
        contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(contentPane);

        // 1. TOPO: Seleção de Carteira Ativa
        JPanel painelTopo = new JPanel(new BorderLayout(10, 10));

        JPanel painelFoco = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelFoco.add(new JLabel("Gerir Conta:"));
        cbCarteiraAtiva = new JComboBox<>();
        cbCarteiraAtiva.setPreferredSize(new Dimension(200, 30));
        painelFoco.add(cbCarteiraAtiva);

        lblSaldoGeral = new JLabel("0.00 EUR", SwingConstants.RIGHT);
        lblSaldoGeral.setFont(new Font("Arial", Font.BOLD, 22));
        lblSaldoGeral.setForeground(new Color(222, 255, 154));

        painelTopo.add(painelFoco, BorderLayout.WEST);
        painelTopo.add(lblSaldoGeral, BorderLayout.EAST);
        contentPane.add(painelTopo, BorderLayout.NORTH);

        // 2. CENTRO: Ativos da Carteira Selecionada
        String[] colunas = {"Ativo (Moeda)", "Quantidade", "Valor Estimado (EUR)"};
        modeloTabela = new DefaultTableModel(colunas, 0);
        tabelaCarteiras = new JTable(modeloTabela);
        tabelaCarteiras.setDefaultEditor(Object.class, null);

        JScrollPane scroll = new JScrollPane(tabelaCarteiras);
        scroll.setBorder(BorderFactory.createTitledBorder("Os Meus Ativos"));
        contentPane.add(scroll, BorderLayout.CENTER);

        // 3. BAIXO: Botões de Operação
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnVerHistorico = new JButton("Histórico");
        btnNovaCarteira = new JButton("Criar Conta");
        btnExchange = new JButton("Swap (Trocar)");
        btnNovaTransacao = new JButton("Transferir");
        btnDeposito = new JButton("Depositar");

        painelBotoes.add(btnVerHistorico);
        painelBotoes.add(btnNovaCarteira);
        painelBotoes.add(btnExchange);
        painelBotoes.add(btnNovaTransacao);
        painelBotoes.add(btnDeposito);
        contentPane.add(painelBotoes, BorderLayout.SOUTH);
    }

    // Métodos para o Controller usar
    public void atualizarListaCarteiras(List<String> nomes) {
        cbCarteiraAtiva.removeAllItems();
        for (String n : nomes) cbCarteiraAtiva.addItem(n);
    }

    public String getCarteiraAtiva() { return (String) cbCarteiraAtiva.getSelectedItem(); }
    public void limparTabela() { modeloTabela.setRowCount(0); }
    public void adicionarAtivo(String moeda, String qtd, String valorEur) {
        modeloTabela.addRow(new Object[]{moeda, qtd, valorEur});
    }
    public void atualizarSaldoTotal(String texto) { lblSaldoGeral.setText(texto); }

    // Getters dos botões
    public JComboBox<String> getCbCarteiraAtiva() { return cbCarteiraAtiva; }
    public JButton getBtnNovaTransacao() { return btnNovaTransacao; }
    public JButton getBtnVerHistorico() { return btnVerHistorico; }
    public JButton getBtnNovaCarteira() { return btnNovaCarteira; }
    public JButton getBtnExchange() { return btnExchange; }
    public JButton getBtnDeposito() { return btnDeposito; }

}