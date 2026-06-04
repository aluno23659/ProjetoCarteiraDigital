package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CarteiraView extends JFrame {

    private JLabel lblSaldoGeral;
    private JButton btnNovaTransacao;
    private JButton btnVerHistorico;

    // Novas peças para a tabela de carteiras
    private JTable tabelaCarteiras;
    private DefaultTableModel modeloTabela;

    public CarteiraView() {
        setTitle("Carteira Digital Pro");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500); // Aumentei a janela para caber a tabela
        setLocationRelativeTo(null);

        // Painel Principal com margens
        JPanel contentPane = new JPanel(new BorderLayout(15, 15));
        contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(contentPane);

        // 1. TOPO: Título e Saldo Geral
        JPanel painelTopo = new JPanel(new GridLayout(2, 1));
        JLabel lblTitulo = new JLabel("Resumo da Conta", SwingConstants.LEFT);
        lblTitulo.setFont(new Font("Urbanist", Font.BOLD, 28));

        lblSaldoGeral = new JLabel("Total: 0.00 EUR");
        lblSaldoGeral.setFont(new Font("Urbanist", Font.PLAIN, 20));
        lblSaldoGeral.setForeground(new Color(222, 255, 154)); // Cor de destaque (Verde FlatLaf)

        painelTopo.add(lblTitulo);
        painelTopo.add(lblSaldoGeral);
        contentPane.add(painelTopo, BorderLayout.NORTH);

        // 2. CENTRO: Tabela de Carteiras
        String[] colunas = {"ID da Carteira", "Saldo Atual", "Moeda"};
        modeloTabela = new DefaultTableModel(colunas, 0);
        tabelaCarteiras = new JTable(modeloTabela);
        tabelaCarteiras.setRowHeight(25);
        tabelaCarteiras.setDefaultEditor(Object.class, null);

        JScrollPane scroll = new JScrollPane(tabelaCarteiras);
        scroll.setBorder(BorderFactory.createTitledBorder("As Minhas Carteiras"));
        contentPane.add(scroll, BorderLayout.CENTER);

        // 3. BAIXO: Botões
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnVerHistorico = new JButton("Ver Histórico");
        btnNovaTransacao = new JButton("Nova Transação");

        // Estética: Botão de transação com destaque
        btnNovaTransacao.putClientProperty("JButton.buttonType", "roundRect");

        painelBotoes.add(btnVerHistorico);
        painelBotoes.add(btnNovaTransacao);
        contentPane.add(painelBotoes, BorderLayout.SOUTH);
    }

    // Métodos para o Controller atualizar a View
    public void limparTabela() { modeloTabela.setRowCount(0); }

    public void adicionarCarteiraTabela(String nome, String saldo, String moeda) {
        modeloTabela.addRow(new Object[]{nome, saldo, moeda});
    }

    public void atualizarSaldoGeral(String texto) {
        lblSaldoGeral.setText(texto);
    }

    public JButton getBtnNovaTransacao() { return btnNovaTransacao; }
    public JButton getBtnVerHistorico() { return btnVerHistorico; }
}