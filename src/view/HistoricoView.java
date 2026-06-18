package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Interface gráfica secundária do tipo diálogo modal para visualização do Histórico de Transações.
 * <p>
 * Esta classe herda de {@link JDialog} e implementa uma janela de auditoria estruturada. 
 * Apresenta em formato tabular todos os fluxos financeiros registados no sistema, incluindo 
 * endereços de conta, ativos movimentados, montantes e o respetivo hash SHA-256 de segurança 
 * que atesta a imutabilidade da transação.
 * </p>
 *
 * @author Seu Nome ou Organização
 * @version 2.0
 * @see javax.swing.JDialog
 */
public class HistoricoView extends JDialog {

    /** Tabela visual para renderização e amostragem das linhas de histórico. */
    private JTable tabela;
    
    /** Modelo lógico estrutural que gere os dados e colunas da tabela. */
    private DefaultTableModel modeloTabela;
    
    /** Botão de ação para exportar o extrato completo de transações em formato de texto puro (TXT). */
    private javax.swing.JButton btnExportar;

    /**
     * Construtor da janela de diálogo HistoricoView.
     * <p>
     * Configura o diálogo como modal para prender o foco da aplicação e organiza uma tabela 
     * com 5 colunas analíticas, definindo o redimensionamento automático de colunas para comportar 
     * a largura dos identificadores criptográficos.
     * </p>
     *
     * @param parent A janela principal ({@link JFrame}) à qual este diálogo se ancora.
     */
    public HistoricoView(JFrame parent) {
        super(parent, "Histórico de Transações Protegido (Blockchain)", true);
        setSize(750, 350); // Aumentou-se a largura da janela para acomodar o Hash longo
        setLocationRelativeTo(parent);

        JPanel painelPrincipal = new JPanel(new BorderLayout());
        painelPrincipal.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Configuração do cabeçalho da tabela (Adicionada a coluna de Hash SHA-256)
        String[] colunas = {"Origem", "Destino", "Moeda", "Valor", "Hash SHA-256"};

        modeloTabela = new DefaultTableModel(colunas, 0);
        tabela = new JTable(modeloTabela);
        tabela.setDefaultEditor(Object.class, null); // Bloqueia a edição direta por parte do utilizador

        // Ajusta a largura da coluna do Hash para otimizar a legibilidade em ecrã
        tabela.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JScrollPane scrollPane = new JScrollPane(tabela);
        painelPrincipal.add(scrollPane, BorderLayout.CENTER);

        add(painelPrincipal);

        btnExportar = new javax.swing.JButton("Exportar Extrato (TXT)");
        // Adiciona o botão de exportação à região inferior da janela de auditoria
        painelPrincipal.add(btnExportar, BorderLayout.SOUTH);
    }

    /**
     * Adiciona uma nova linha de registo na tabela de transações, aplicando um algoritmo 
     * de encurtamento visual para strings de hash extensas.
     * <p>
     * Para prevenir a degradação da interface e estouro de colunas, o método extrai e exibe 
     * apenas os primeiros 15 caracteres do hash gerado, concatenando-os com uma elipse ("...").
     * </p>
     *
     * @param origem  O endereço alfanumérico da carteira remetente.
     * @param destino O endereço alfanumérico da carteira destinatária.
     * @param moeda   O nome ou ticker de mercado do ativo transacionado.
     * @param valor   O montante numérico formatado como texto.
     * @param hash    A assinatura criptográfica digital SHA-256 completa da transação.
     */
    public void adicionarLinha(String origem, String destino, String moeda, String valor, String hash) {
        // Mostra apenas os primeiros 15 caracteres do Hash para não sobrecarregar o ecrã, seguido de "..."
        String hashCurto = hash.substring(0, Math.min(hash.length(), 15)) + "...";
        modeloTabela.addRow(new Object[]{origem, destino, moeda, valor, hashCurto});
    }

    /**
     * Devolve o botão de gatilho para a exportação de ficheiros de extrato.
     *
     * @return O componente {@link JButton} associado para atribuição de eventos no controlador.
     */
    public javax.swing.JButton getBtnExportar() {
        return btnExportar;
    }
}   