package controller;

import model.data.CsvLedger;
import view.CarteiraView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

public class CarteiraController {

    private CarteiraView view;
    private CsvLedger ledger;

    // O Construtor recebe a janela e o motor de dados
    public CarteiraController(CarteiraView view, CsvLedger ledger) {
        this.view = view;
        this.ledger = ledger;

        // Iniciar as escutas dos botões
        inicializarEventos();
    }

    private void inicializarEventos() {
        // Quando o botão "Ver Histórico" for clicado na View, este código é executado
        view.getBtnVerHistorico().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Para já, mostra apenas um aviso simples
                JOptionPane.showMessageDialog(view, "O histórico tem " + ledger.getElements().size() + " transações gravadas!");
            }
        });

        // Quando o botão "Nova Transação" for clicado
        view.getBtnNovaTransacao().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(view, "A janela de nova transação vai aparecer aqui!");
            }
        });
    }
}