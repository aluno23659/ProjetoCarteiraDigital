package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class HistoricoView extends JDialog {

    private JTable tabela;
    private DefaultTableModel modeloTabela;

    public HistoricoView(JFrame parent) {
        super(parent, "Histórico de Transações Protegido (Blockchain)", true);
        setSize(750, 350); // Aumentei a largura da janela para caber o Hash longo
        setLocationRelativeTo(parent);

        JPanel painelPrincipal = new JPanel(new BorderLayout());
        painelPrincipal.setBorder(new EmptyBorder(15, 15, 15, 15));

        // ADICIONADA A COLUNA "Hash SHA-256"
        String[] colunas = {"Origem", "Destino", "Moeda", "Valor", "Hash SHA-256"};

        modeloTabela = new DefaultTableModel(colunas, 0);
        tabela = new JTable(modeloTabela);
        tabela.setDefaultEditor(Object.class, null);

        // Ajustar a largura da coluna do Hash para ficar legível
        tabela.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JScrollPane scrollPane = new JScrollPane(tabela);
        painelPrincipal.add(scrollPane, BorderLayout.CENTER);

        add(painelPrincipal);
    }

    // Método atualizado para receber o Hash
    public void adicionarLinha(String origem, String destino, String moeda, String valor, String hash) {
        // Mostra apenas os primeiros 15 caracteres do Hash para não sobrecarregar o ecrã, seguido de "..."
        String hashCurto = hash.substring(0, Math.min(hash.length(), 15)) + "...";
        modeloTabela.addRow(new Object[]{origem, destino, moeda, valor, hashCurto});
    }
}