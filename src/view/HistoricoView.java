package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class HistoricoView extends JDialog {

    private JTable tabela;
    private DefaultTableModel modeloTabela;

    public HistoricoView(JFrame parent) {
        super(parent, "Histórico de Transações", true);
        setSize(550, 300);
        setLocationRelativeTo(parent);

        // 1. Painel principal com margens limpas
        JPanel painelPrincipal = new JPanel(new BorderLayout());
        painelPrincipal.setBorder(new EmptyBorder(15, 15, 15, 15));

        // 2. Definir as colunas da nossa tabela
        String[] colunas = {"Origem", "Destino", "Moeda", "Valor"};

        // O DefaultTableModel é o "esqueleto" que guarda os dados da tabela
        modeloTabela = new DefaultTableModel(colunas, 0);
        tabela = new JTable(modeloTabela);

        // Impedir que o utilizador edite as células diretamente na tabela
        tabela.setDefaultEditor(Object.class, null);

        // 3. A Magia do Swing: Embrulhar a tabela num Scroll para ter cabeçalhos e barra de rolagem
        JScrollPane scrollPane = new JScrollPane(tabela);
        painelPrincipal.add(scrollPane, BorderLayout.CENTER);

        add(painelPrincipal);
    }

    // Método público para o Controller poder injetar as transações uma a uma
    public void adicionarLinha(String origem, String destino, String moeda, String valor) {
        modeloTabela.addRow(new Object[]{origem, destino, moeda, valor});
    }
}