package application;

import controller.CarteiraController;
import model.data.CsvLedger;
import view.CarteiraView;
import com.formdev.flatlaf.FlatDarkLaf;

/**
 * Ponto de entrada principal da aplicação da Carteira.
 * <p>
 * Esta classe é responsável por inicializar o visual da aplicação (Look and Feel),
 * carregar o repositório de dados, instanciar a interface gráfica e conectar
 * os componentes através do padrão MVC (Model-View-Controller).
 * </p>
 * * @author Sidnei e Jose
 * @version 1.0
 */
public class Main {

    /**
     * Método principal que inicializa e orquestra o arranque da aplicação.
     * <p>
     * O fluxo de inicialização segue rigorosamente a ordem:
     * 1. Configuração do tema visual (Look and Feel)
     * 2. Inicialização do Model (CsvLedger)
     * 3. Inicialização da View (CarteiraView)
     * 4. Inicialização do Controller (CarteiraController)
     * 5. Exibição da interface gráfica
     * </p>
     *
     * @param args Argumentos de linha de comando passados para a aplicação (não utilizados).
     */
    public static void main(String[] args) {

        // 1. A SKIN TEM DE ESTAR AQUI (Antes de qualquer outra coisa ser renderizada)
        FlatDarkLaf.setup();

        // 2. Iniciar o MODEL (Gerenciador de dados do arquivo CSV)
        CsvLedger ledger = new CsvLedger("historico_transacoes.csv");

        // 3. Iniciar a VIEW (Interface gráfica)
        CarteiraView view = new CarteiraView();

        // 4. Iniciar o CONTROLLER (Une a View ao Model)
        CarteiraController controller = new CarteiraController(view, ledger);

        // 5. Mostrar a janela para o usuário
        view.setVisible(true);
    }
}