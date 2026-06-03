package application;

import controller.CarteiraController;
import model.data.CsvLedger;
import view.CarteiraView;
import com.formdev.flatlaf.FlatDarkLaf; // Ajuda importar diretamente aqui

public class Main {

    public static void main(String[] args) {

        // 1. A SKIN TEM DE ESTAR AQUI (Antes de qualquer outra coisa)
        FlatDarkLaf.setup();

        // 2. Iniciar o MODEL
        CsvLedger ledger = new CsvLedger("historico_transacoes.csv");

        // 3. Iniciar a VIEW
        CarteiraView view = new CarteiraView();

        // 4. Iniciar o CONTROLLER
        CarteiraController controller = new CarteiraController(view, ledger);

        // 5. Mostrar a janela
        view.setVisible(true);
    }
}