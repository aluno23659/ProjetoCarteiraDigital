package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Interface gráfica secundária do tipo diálogo modal para operações de Câmbio (Swap).
 * <p>
 * Esta classe herda de {@link JDialog} e é responsável por renderizar um formulário suspenso 
 * e focado. Permite ao utilizador selecionar a moeda de origem a ser vendida, a moeda de 
 * destino a ser adquirida e introduzir o montante numérico para processamento da troca.
 * </p>
 *
 * @author Seu Nome ou Organização
 * @version 1.0
 * @see javax.swing.JDialog
 */
public class ExchangeView extends JDialog {

    /** Caixa de seleção (Dropdown) para a moeda ou ativo que será vendido. */
    private JComboBox<String> cbMoedaOrigem;
    
    /** Caixa de seleção (Dropdown) para a moeda ou ativo que será comprado. */
    private JComboBox<String> cbMoedaDestino;
    
    /** Campo de texto destinado à inserção da quantidade a ser convertida. */
    private JTextField txtQuantidade;
    
    /** Botão de ação para submeter e confirmar a ordem de troca cambial. */
    private JButton btnConfirmar;

    /**
     * Construtor da janela de diálogo ExchangeView.
     * <p>
     * Configura o diálogo como modal (bloqueia a interação com a janela principal {@link JFrame} 
     * enquanto estiver aberto) e distribui os rótulos e campos de input numa estrutura tabular 
     * simétrica através de um layout de grelha ({@link GridLayout}).
     * </p>
     *
     * @param parent A janela principal ({@link JFrame}) à qual este diálogo pertence e se ancora.
     */
    public ExchangeView(JFrame parent) {
        super(parent, "Crypto Swap (Exchange Interno)", true);
        setSize(350, 250);
        setLocationRelativeTo(parent);

        // Define uma grelha de 4 linhas por 2 colunas, com espaçamentos de 10px e 15px
        JPanel p = new JPanel(new GridLayout(4, 2, 10, 15));
        p.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Configuração da linha 1: Ativo de Origem
        p.add(new JLabel("Vender (De):"));
        cbMoedaOrigem = new JComboBox<>(new String[]{"Bitcoin (BTC)", "Ethereum (ETH)", "Euro (EUR)"});
        p.add(cbMoedaOrigem);

        // Configuração da linha 2: Ativo de Destino
        p.add(new JLabel("Comprar (Para):"));
        cbMoedaDestino = new JComboBox<>(new String[]{"Ethereum (ETH)", "Bitcoin (BTC)", "Euro (EUR)"});
        p.add(cbMoedaDestino);

        // Configuração da linha 3: Quantidade de Ativos
        p.add(new JLabel("Quantidade:"));
        txtQuantidade = new JTextField();
        p.add(txtQuantidade);

        // Configuração da linha 4: Espaço vazio e Botão de Submissão
        p.add(new JLabel(""));
        btnConfirmar = new JButton("Confirmar Troca");
        p.add(btnConfirmar);

        add(p);
    }

    /**
     * Recupera o texto da moeda selecionada no dropdown de venda (origem).
     *
     * @return A String correspondente ao item selecionado (ex: "Bitcoin (BTC)").
     */
    public String getMoedaOrigem() { 
        return (String) cbMoedaOrigem.getSelectedItem(); 
    }

    /**
     * Recupera o texto da moeda selecionada no dropdown de compra (destino).
     *
     * @return A String correspondente ao item selecionado (ex: "Euro (EUR)").
     */
    public String getMoedaDestino() { 
        return (String) cbMoedaDestino.getSelectedItem(); 
    }

    /**
     * Obtém o conteúdo de texto introduzido pelo utilizador no campo de quantidade.
     * <p>
     * <b>Nota de Desenvolvimento:</b> O retorno deve ser validado e convertido para 
     * um tipo numérico ({@code double}) no controlador antes de realizar os cálculos.
     * </p>
     *
     * @return A String pura contendo o valor do campo de input.
     */
    public String getQuantidade() { 
        return txtQuantidade.getText(); 
    }

    /**
     * Devolve o botão de gatilho para a confirmação da operação de swap.
     *
     * @return O componente {@link JButton} associado para vinculação de ActionListeners.
     */
    public JButton getBtnConfirmar() { 
        return btnConfirmar; 
    }
}