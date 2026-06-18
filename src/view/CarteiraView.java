package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Interface gráfica principal da aplicação (Janela de Dashboard da Carteira).
 * <p>
 * Esta classe herda de {@link JFrame} e implementa a visualização central do ecossistema. 
 * Fornece ao utilizador um balanço total consolidado, uma tabela para listagem analítica de 
 * ativos e botões de gatilho para operações como depósitos, levantamentos, trocas (swaps) e transferências.
 * </p>
 *
 * @author Seu Nome ou Organização
 * @version 2.0
 * @see javax.swing.JFrame
 */
public class CarteiraView extends JFrame {

    /** Rótulo textual que apresenta o saldo total consolidado (convertido em Euros). */
    private JLabel lblSaldoGeral;
    
    /** Caixa de seleção (Dropdown) contendo os endereços das carteiras disponíveis. */
    private JComboBox<String> cbCarteiraAtiva;
    
    /** Botão de ação para disparar uma nova transferência entre utilizadores. */
    private JButton btnNovaTransacao;
    
    /** Botão de ação para abrir a janela com o histórico completo de transações. */
    private JButton btnVerHistorico;
    
    /** Botão de ação para inicializar e registar uma nova carteira no sistema. */
    private JButton btnNovaCarteira;
    
    /** Botão de ação para abrir a janela de conversão cambial (Swap) de ativos. */
    private JButton btnExchange;
    
    /** Botão de ação para efetuar um depósito (injeção de capital) de sistema. */
    private JButton btnDeposito;
    
    /** Botão de ação para efetuar um levantamento de fundos. */
    private JButton btnLevantamento;
    
    /** Tabela visual para amostragem dos fundos/criptoativos pertencentes à conta ativa. */
    private JTable tabelaCarteiras;
    
    /** Modelo lógico de dados associado à tabela de ativos. */
    private DefaultTableModel modeloTabela;

    /**
     * Construtor padrão da visualização da carteira.
     * <p>
     * Configura as propriedades básicas da janela (título, dimensões e posicionamento) 
     * e organiza os componentes visuais em três zonas de layout principais:
     * <ul>
     * <li><b>NORTH (Topo):</b> Seleção da conta ativa e o saldo total formatado.</li>
     * <li><b>CENTER (Centro):</b> Tabela em scroll listando os ativos e saldos parciais.</li>
     * <li><b>SOUTH (Baixo):</b> Painel de fluxo com os botões operacionais da aplicação.</li>
     * </ul>
     * </p>
     */
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
        tabelaCarteiras.setDefaultEditor(Object.class, null); // Bloqueia a edição direta das células

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
        btnLevantamento = new JButton("Levantamento");

        painelBotoes.add(btnVerHistorico);
        painelBotoes.add(btnNovaCarteira);
        painelBotoes.add(btnExchange);
        painelBotoes.add(btnNovaTransacao);
        painelBotoes.add(btnDeposito);
        painelBotoes.add(btnLevantamento);
        contentPane.add(painelBotoes, BorderLayout.SOUTH);
    }

    /**
     * Atualiza os elementos da combobox de seleção de carteiras com uma nova lista de identificadores.
     *
     * @param nomes Lista contendo as strings dos nomes/endereços das carteiras registadas.
     */
    public void atualizarListaCarteiras(List<String> nomes) {
        cbCarteiraAtiva.removeAllItems();
        for (String n : nomes) {
            cbCarteiraAtiva.addItem(n);
        }
    }

    /**
     * Recupera o endereço da carteira que está atualmente selecionada no dropdown.
     *
     * @return O texto (String) correspondente ao item ativo.
     */
    public String getCarteiraAtiva() { 
        return (String) cbCarteiraAtiva.getSelectedItem(); 
    }

    /**
     * Remove todos os registos e linhas atualmente visíveis na tabela de ativos.
     */
    public void limparTabela() { 
        modeloTabela.setRowCount(0); 
    }

    /**
     * Adiciona uma nova linha com dados de um ativo financeiro na tabela de visualização.
     *
     * @param moeda    O nome ou sigla da moeda (ex: "BitCoin(BTC)").
     * @param qtd      A quantidade líquida existente em posse.
     * @param valorEur O contravalor financeiro estimado calculado em Euros.
     */
    public void adicionarAtivo(String moeda, String qtd, String valorEur) {
        modeloTabela.addRow(new Object[]{moeda, qtd, valorEur});
    }

    /**
     * Modifica o texto do rótulo do balanço geral para refletir o montante atualizado.
     *
     * @param texto String contendo o valor e a moeda fiduciária formatados (ex: "1,250.50 EUR").
     */
    public void atualizarSaldoTotal(String texto) { 
        lblSaldoGeral.setText(texto); 
    }

    /**
     * Devolve a combobox responsável pela gestão de seleção de contas.
     *
     * @return O objeto {@link JComboBox} de carteiras.
     */
    public JComboBox<String> getCbCarteiraAtiva() { return cbCarteiraAtiva; }

    /**
     * Devolve o gatilho de criação de novas transferências.
     *
     * @return O componente {@link JButton} associado.
     */
    public JButton getBtnNovaTransacao() { return btnNovaTransacao; }

    /**
     * Devolve o gatilho de abertura de listagem de históricos.
     *
     * @return O componente {@link JButton} associado.
     */
    public JButton getBtnVerHistorico() { return btnVerHistorico; }

    /**
     * Devolve o gatilho para criação de novas contas/carteiras.
     *
     * @return O componente {@link JButton} associado.
     */
    public JButton getBtnNovaCarteira() { return btnNovaCarteira; }

    /**
     * Devolve o gatilho para abertura de ordens de câmbio (Swap).
     *
     * @return O componente {@link JButton} associado.
     */
    public JButton getBtnExchange() { return btnExchange; }

    /**
     * Devolve o gatilho para ordens de depósitos de fundos.
     *
     * @return O componente {@link JButton} associado.
     */
    public JButton getBtnDeposito() { return btnDeposito; }

    /**
     * Devolve o gatilho para ordens de levantamento de fundos.
     *
     * @return O componente {@link JButton} associado.
     */
    public JButton getBtnLevantamento() { return btnLevantamento; }
}