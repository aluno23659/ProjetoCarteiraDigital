package controller;

import model.data.CsvLedger;
import view.CarteiraView;
import model.transactions.Transaction;
import model.exchange.ConversorMoeda;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

public class CarteiraController {

    private CarteiraView view;
    private CsvLedger ledger;

    public CarteiraController(CarteiraView view, CsvLedger ledger) {
        this.view = view;
        this.ledger = ledger;

        inicializarEventos();

        // Assim que o Controller arranca, ele faz as contas e atualiza o ecrã na hora!
        calcularEAtualizarSaldo();
    }

    private void inicializarEventos() {
        // Quando o botão "Ver Histórico" for clicado
        view.getBtnVerHistorico().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 1. Cria a nova janela de Histórico
                view.HistoricoView janelaHistorico = new view.HistoricoView(view);

                // 2. Vai ao Motor de Dados (Ledger) buscar a lista toda
                for (Transaction t : ledger.getElements()) {

                    // --- ATENÇÃO AOS NOMES DOS MÉTODOS AQUI ---
                    // Como não conheço a tua classe Transaction, se estes métodos ficarem a vermelho,
                    // altera-os para os getters corretos (ex: t.getOrigem(), t.getSender(), etc.)
                    // O ".toString()" garante que convertemos o objeto para texto legível.

                    String origem = t.getSource().toString();
                    String destino = t.getDestination().toString();
                    String moeda = t.getCoin().toString();
                    String valor = String.valueOf(t.getAmount());

                    // 3. Envia os dados preparados para a View desenhar na tabela
                    janelaHistorico.adicionarLinha(origem, destino, moeda, valor);
                }

                // 4. Mostra a janela preenchida
                janelaHistorico.setVisible(true);
            }
        });;

        // Quando o botão "Nova Transação" da janela principal for clicado
        view.getBtnNovaTransacao().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 1. Abre a nova janela de formulário
                view.NovaTransacaoView janelaNova = new view.NovaTransacaoView(view);

                // 2. Fica à escuta do botão "Gravar"
                janelaNova.getBtnGravar().addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent ev) {

                        // a) Ler os textos e limpar espaços em branco acidentais com o .trim()
                        String origem = janelaNova.getOrigem().trim();
                        String destino = janelaNova.getDestino().trim();
                        String valorTexto = janelaNova.getValor().trim();

                        // --- FASE DE VALIDAÇÃO ---

                        // VALIDAÇÃO 1: Campos Vazios?
                        if (origem.isEmpty() || destino.isEmpty() || valorTexto.isEmpty()) {
                            JOptionPane.showMessageDialog(janelaNova,
                                    "Atenção: Todos os campos são de preenchimento obrigatório!",
                                    "Dados Incompletos",
                                    JOptionPane.WARNING_MESSAGE);
                            return; // Corta a execução aqui. O código abaixo já não corre.
                        }

                        try {
                            // VALIDAÇÃO 2: É um número a sério?
                            double valor = Double.parseDouble(valorTexto);

                            // VALIDAÇÃO 3: Faz sentido? (Não transferir zero nem valores negativos)
                            if (valor <= 0) {
                                JOptionPane.showMessageDialog(janelaNova,
                                        "Erro: O valor da transação tem de ser maior que zero.",
                                        "Valor Inválido",
                                        JOptionPane.ERROR_MESSAGE);
                                return;
                            }

                            // --- SE PASSOU AS VALIDAÇÕES TODAS, GRAVA! ---

                            model.wallet.RegularWallet cartOrigem = new model.wallet.RegularWallet(origem);
                            model.wallet.RegularWallet cartDestino = new model.wallet.RegularWallet(destino);

                            // 1. Descobrir qual foi a moeda que o utilizador escolheu no Dropdown
                            String moedaEscolhida = janelaNova.getMoedaSelecionada();
                            model.coin.Currency moedaObj;

                            // 2. Criar a moeda certa consoante a escolha
                            // ATENÇÃO: Verifica se tens estas classes (Ethereum, Euro) criadas no teu pacote model.coin!
                            if (moedaEscolhida.contains("BTC")) {
                                moedaObj = new model.coin.BitCoin();
                            } else if (moedaEscolhida.contains("ETH")) {
                                moedaObj = new model.coin.Ether();
                            } else {
                                moedaObj = new model.coin.Euro();
                            }

                            // 3. Criar a transação com a moeda dinâmica
                            Transaction t = new Transaction(cartOrigem, cartDestino, moedaObj, valor);

                            ledger.add(t);

                            calcularEAtualizarSaldo();
                            janelaNova.dispose();

                            JOptionPane.showMessageDialog(view, "Transação gravada com sucesso!");

                        } catch (NumberFormatException ex) {
                            // Se o Double.parseDouble falhar porque o utilizador escreveu "letras" em vez de números
                            JOptionPane.showMessageDialog(janelaNova,
                                    "Erro: Escreve um número válido no valor (ex: 2.5)",
                                    "Formato de Número Inválido",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });

                // Mostra a janela no ecrã
                janelaNova.setVisible(true);
            }
        });
    }

    // O CÉREBRO EM AÇÃO: LER DO MODELO, CALCULAR E ENVIAR PARA A VIEW
    private void calcularEAtualizarSaldo() {
        double saldoTotalEuros = 0.0;

        // Percorre todas as transações que o Ledger carregou do CSV
        for (Transaction t : ledger.getElements()) {

            // ATENÇÃO: Verifica se os métodos getAmount() e getCoin() são os corretos
            // da tua classe Transaction feita pelo professor. Se ele usou outros nomes, altera aqui.
            double quantidade = t.getAmount();
            model.coin.Currency moeda = t.getCoin();

            // Usa o teu conversor da Fase 2 para transformar o valor da transação em Euros
            double valorEmEuros = ConversorMoeda.paraEuro(moeda, quantidade);

            // Soma ao saldo total
            saldoTotalEuros += valorEmEuros;
        }

        // Formata o número para ter apenas 2 casas decimais (ex: 150000.00 EUR)
        String saldoFormatado = String.format("%.2f", saldoTotalEuros);

        // Dá a ordem à Janela (View) para mudar o texto
        view.atualizarSaldo("Saldo Total: " + saldoFormatado + " EUR");
    }
}
