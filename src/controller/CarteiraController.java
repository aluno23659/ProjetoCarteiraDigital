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
        // 1. Estruturas para organizar os dados
        // Chave: Nome da Carteira | Valor: Saldo acumulado
        java.util.Map<String, Double> saldosPorCarteira = new java.util.HashMap<>();
        double saldoTotalGeralEuros = 0.0;

        // 2. Percorrer o histórico e fazer as contas
        for (Transaction t : ledger.getElements()) {
            String origem = t.getSource().toString();
            String destino = t.getDestination().toString();
            double valor = t.getAmount();
            model.coin.Currency moeda = t.getCoin();

            // Lógica: Quem envia (origem) perde saldo, quem recebe (destino) ganha
            saldosPorCarteira.put(origem, saldosPorCarteira.getOrDefault(origem, 0.0) - valor);
            saldosPorCarteira.put(destino, saldosPorCarteira.getOrDefault(destino, 0.0) + valor);

            // Somar para o saldo geral em Euros (Usa o teu conversor da Fase 2!)
            // Nota: Aqui calculamos o valor absoluto de cada transação no sistema
            saldoTotalGeralEuros += ConversorMoeda.paraEuro(moeda, valor);
        }

        // 3. Atualizar a Tabela na View
        view.limparTabela();
        for (String carteira : saldosPorCarteira.keySet()) {
            double saldoFinal = saldosPorCarteira.get(carteira);
            // Mostramos o saldo na moeda padrão (BTC para este exemplo, ou podes adaptar)
            view.adicionarCarteiraTabela(carteira, String.format("%.4f", saldoFinal), "BTC/ETH");
        }

        // 4. Atualizar o Saldo Geral
        view.atualizarSaldoGeral("Total Consolidado: " + String.format("%.2f", saldoTotalGeralEuros) + " EUR");
    }
}
