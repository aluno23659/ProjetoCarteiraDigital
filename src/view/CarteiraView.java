package view;

import javax.swing.*;
import java.awt.*;

// O "extends JFrame" significa que esta classe É uma janela do sistema operativo
public class CarteiraView extends JFrame {

    // 1. Declarar os componentes visuais (Encapsulamento: sempre private!)
    private JLabel lblTitulo;
    private JLabel lblSaldoGeral;
    private JButton btnNovaTransacao;
    private JButton btnVerHistorico;

    // 2. Construtor: Aqui é onde "desenhamos" a janela
    public CarteiraView() {
        // Configurações básicas da janela
        setTitle("Carteira Digital");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Fechar o programa quando fechar a janela
        setSize(400, 300); // Largura x Altura
        setLocationRelativeTo(null); // Faz a janela aparecer perfeitamente centrada no ecrã
        setLayout(new BorderLayout(10, 10)); // Um gestor de layout limpo, com margens

        // Inicializar os componentes
        lblTitulo = new JLabel("A Minha Carteira", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));

        lblSaldoGeral = new JLabel("Saldo: -- EUR", SwingConstants.CENTER);
        lblSaldoGeral.setFont(new Font("Segoe UI", Font.PLAIN, 18));

        btnNovaTransacao = new JButton("Nova Transação");
        btnVerHistorico = new JButton("Ver Histórico");

        // Painel para organizar os botões em baixo
        JPanel painelBotoes = new JPanel();
        painelBotoes.add(btnNovaTransacao);
        painelBotoes.add(btnVerHistorico);

        // Adicionar tudo à Janela Principal
        add(lblTitulo, BorderLayout.NORTH);
        add(lblSaldoGeral, BorderLayout.CENTER);
        add(painelBotoes, BorderLayout.SOUTH);
    }

    // 3. Métodos Públicos (Para o Controller poder atualizar a View no futuro)
    public void atualizarSaldo(String texto) {
        lblSaldoGeral.setText(texto);
    }

    // Main temporário apenas para testares o visual da janela
    public static void main(String[] args) {
        // Inicialização oficial do FlatLaf (Muda para FlatDarkLaf.setup() se quiseres modo escuro)
        com.formdev.flatlaf.FlatDarkLaf.setup();

        // Criar e mostrar a janela
        SwingUtilities.invokeLater(() -> {
            CarteiraView janela = new CarteiraView();
            janela.setVisible(true);
        });
    }
    // Getters para o Controller conseguir aceder aos botões
    public JButton getBtnNovaTransacao() {
        return btnNovaTransacao;
    }

    public JButton getBtnVerHistorico() {
        return btnVerHistorico;
    }
}