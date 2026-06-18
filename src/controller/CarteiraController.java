package controller;

import model.data.CsvLedger;
import view.CarteiraView;
import model.transactions.Transaction;
import model.exchange.ConversorMoeda;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Controlador principal da interface da Carteira (Camada Controller do padrão MVC).
 * <p>
 * Esta classe gere o ciclo de vida dos eventos gerados pela interface gráfica (View),
 * valida as regras de negócio de depósitos, levantamentos, transferências e câmbios,
 * e atualiza o histórico persistido no Ledger (Model).
 * </p>
 *
 * @author Sidnei e Jose
 * @version 2.0
 */
public class CarteiraController {

    /** Janela principal da interface gráfica da carteira. */
    private final CarteiraView view;
    
    /** Gestor de persistência e histórico de transações em ficheiro CSV. */
    private final CsvLedger ledger;
    
    /** Lista em memória contendo os nomes de todas as carteiras registadas e ativas. */
    private final java.util.List<String> carteirasRegistadas;

    /**
     * Construtor do Controlador. Inicializa os dados, mapeia eventos e atualiza o ecrã.
     *
     * @param view   A janela principal da aplicação (Interface Gráfica).
     * @param ledger O livro de registos das transações (Persistência).
     */
    public CarteiraController(CarteiraView view, model.data.CsvLedger ledger) {
        this.view = view;
        this.ledger = ledger;
        this.carteirasRegistadas = new java.util.ArrayList<>();

        carregarCarteirasDoHistorico();

        // Injeta os nomes das carteiras no menu pendente
        view.atualizarListaCarteiras(this.carteirasRegistadas);

        inicializarEventos();
        calcularEAtualizarSaldo();
    }

    /**
     * Percorre o histórico de transações no Ledger para descobrir e extrair
     * os nomes de todas as carteiras que já realizaram transações, preenchendo a lista em memória.
     */
    private void carregarCarteirasDoHistorico() {
        for (model.transactions.Transaction t : ledger.getElements()) {
            String origem = t.getSource().toString().replace("(true)", "").replace("(false)", "").trim();
            String destino = t.getDestination().toString().replace("(true)", "").replace("(false)", "").trim();

            if (!carteirasRegistadas.contains(origem)) carteirasRegistadas.add(origem);
            if (!carteirasRegistadas.contains(destino)) carteirasRegistadas.add(destino);
        }
    }

    /**
     * Vincula os ActionListeners aos botões e componentes da interface gráfica.
     * Define o comportamento ao interagir com depósitos, levantamentos, swaps, novas transações e histórico.
     */
    private void inicializarEventos() {
        
        // 1. Atualizar a tabela ao mudar a carteira ativa no Dropdown
        view.getCbCarteiraAtiva().addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                calcularEAtualizarSaldo();
            }
        });

        // 2. Lógica do Botão DEPOSITAR
        view.getBtnDeposito().addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                String ativa = view.getCarteiraAtiva();
                if (ativa == null) {
                    JOptionPane.showMessageDialog(view, "Seleciona uma carteira primeiro!");
                    return;
                }

                JPanel painelDeposito = new JPanel(new GridLayout(2, 2, 5, 5));
                painelDeposito.add(new JLabel("Moeda:"));
                JComboBox<String> cbMoeda = new JComboBox<>(new String[]{"Euro (EUR)", "Bitcoin (BTC)", "Ethereum (ETH)"});
                painelDeposito.add(cbMoeda);

                painelDeposito.add(new JLabel("Valor a depositar:"));
                JTextField txtValor = new JTextField();
                painelDeposito.add(txtValor);

                int resultado = JOptionPane.showConfirmDialog(view, painelDeposito,
                        "Depósito Externo", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                if (resultado == JOptionPane.OK_OPTION) {
                    try {
                        double valor = Double.parseDouble(txtValor.getText().replace(",", "."));
                        if (valor <= 0) throw new NumberFormatException(); 

                        String moedaEscolhida = (String) cbMoeda.getSelectedItem();
                        model.coin.Currency moedaObj = model.coin.CoinFactory.criarMoeda(moedaEscolhida);

                        model.wallet.Wallet origem = new model.wallet.RegularWallet("Entidade_Bancaria");
                        model.wallet.Wallet destino = new model.wallet.RegularWallet(ativa);

                        ledger.add(new model.transactions.Transaction(origem, destino, moedaObj, valor));

                        calcularEAtualizarSaldo();
                        JOptionPane.showMessageDialog(view, "Depósito de " + valor + " registado com sucesso!");

                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(view, "Valor inválido. Insere apenas números maiores que zero.", "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // 3. Lógica do Botão LEVANTAR (WITHDRAW)
        view.getBtnLevantamento().addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                String ativa = view.getCarteiraAtiva();
                if (ativa == null) {
                    JOptionPane.showMessageDialog(view, "Seleciona uma carteira primeiro!");
                    return;
                }

                JPanel painelLevantamento = new JPanel(new GridLayout(2, 2, 5, 5));
                painelLevantamento.add(new JLabel("Moeda:"));
                JComboBox<String> cbMoeda = new JComboBox<>(new String[]{"Euro (EUR)", "Bitcoin (BTC)", "Ethereum (ETH)"});
                painelLevantamento.add(cbMoeda);

                painelLevantamento.add(new JLabel("Valor a levantar:"));
                JTextField txtValor = new JTextField();
                painelLevantamento.add(txtValor);

                int resultado = JOptionPane.showConfirmDialog(view, painelLevantamento,
                        "Levantamento Externo", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                if (resultado == JOptionPane.OK_OPTION) {
                    try {
                        double valor = Double.parseDouble(txtValor.getText().replace(",", "."));
                        if (valor <= 0) throw new NumberFormatException();

                        String moedaEscolhida = (String) cbMoeda.getSelectedItem();
                        model.coin.Currency moedaObj = model.coin.CoinFactory.criarMoeda(moedaEscolhida);

                        // VALIDAÇÃO CRÍTICA DE SALDO
                        double saldoDisponivel = obterSaldoDaMoeda(ativa, moedaObj.toString());

                        if (valor > saldoDisponivel) {
                            throw new exceptions.SaldoInsuficienteException(
                                "Saldo insuficiente! Tens apenas " + saldoDisponivel + " " + moedaObj.toString() + " nesta carteira."
                            );
                        }

                        model.wallet.Wallet origem = new model.wallet.RegularWallet(ativa);
                        model.wallet.Wallet destino = new model.wallet.RegularWallet("Levantamento_Externo");

                        // Regista o levantamento com valor positivo. A lógica de cálculo de saldo trata da subtração.
                        ledger.add(new model.transactions.Transaction(origem, destino, moedaObj, valor));

                        calcularEAtualizarSaldo();
                        JOptionPane.showMessageDialog(view, "Levantamento de " + valor + " registado com sucesso!");

                    } catch (exceptions.SaldoInsuficienteException ex) {
                        JOptionPane.showMessageDialog(view, ex.getMessage(), "Erro de Saldo", JOptionPane.ERROR_MESSAGE);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(view, "Valor inválido. Insere apenas números maiores que zero.", "Erro", JOptionPane.ERROR_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(view, "Ocorreu um erro ao processar o levantamento.", "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // 4. Lógica do Botão SWAP (Trocar moedas)
        view.getBtnExchange().addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                String ativa = view.getCarteiraAtiva();
                if (ativa == null) {
                    JOptionPane.showMessageDialog(view, "Cria ou seleciona uma carteira primeiro!");
                    return;
                }

                view.ExchangeView janelaSwap = new view.ExchangeView(view);
                janelaSwap.getBtnConfirmar().addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent ev) {
                        try {
                            double qtdVenda = Double.parseDouble(janelaSwap.getQuantidade().replace(",", "."));
                            model.coin.Currency moedaDe = model.coin.CoinFactory.criarMoeda(janelaSwap.getMoedaOrigem());
                            model.coin.Currency moedaPara = model.coin.CoinFactory.criarMoeda(janelaSwap.getMoedaDestino());

                            model.wallet.Wallet w = new model.wallet.RegularWallet(ativa);

                            // Regista a SAÍDA (venda)
                            ledger.add(new model.transactions.Transaction(w, w, moedaDe, -qtdVenda));

                            // Taxas de Câmbio Fixas para Simulação
                            double taxa = 1.0;
                            String nomeDe = moedaDe.toString();
                            String nomePara = moedaPara.toString();

                            if (nomeDe.contains("BTC") && nomePara.contains("ETH")) taxa = 27.74;
                            else if (nomeDe.contains("ETH") && nomePara.contains("BTC")) taxa = 0.036;
                            else if (nomeDe.contains("BTC") && nomePara.contains("EUR")) taxa = 61000.0;
                            else if (nomeDe.contains("EUR") && nomePara.contains("BTC")) taxa = 0.000016;

                            double qtdCompra = qtdVenda * taxa;

                            // Regista a ENTRADA (compra)
                            ledger.add(new model.transactions.Transaction(w, w, moedaPara, qtdCompra));

                            calcularEAtualizarSaldo();
                            janelaSwap.dispose();
                            JOptionPane.showMessageDialog(view, "Swap realizado com sucesso!");

                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(janelaSwap, "Verifica o valor inserido.");
                        }
                    }
                });
                janelaSwap.setVisible(true);
            }
        });

        // 5. Lógica do Botão CRIAR CARTEIRA
        view.getBtnNovaCarteira().addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                String nomeNovaCarteira = JOptionPane.showInputDialog(view, "Qual o nome da nova carteira?");

                if (nomeNovaCarteira != null && !nomeNovaCarteira.trim().isEmpty()) {
                    String nomeLimpo = nomeNovaCarteira.trim();

                    if (carteirasRegistadas.contains(nomeLimpo)) {
                        JOptionPane.showMessageDialog(view, "Esta carteira já existe!", "Erro", JOptionPane.ERROR_MESSAGE);
                    } else {
                        carteirasRegistadas.add(nomeLimpo);
                        view.atualizarListaCarteiras(carteirasRegistadas);
                        view.getCbCarteiraAtiva().setSelectedItem(nomeLimpo);
                        JOptionPane.showMessageDialog(view, "Carteira '" + nomeLimpo + "' criada com sucesso!");
                    }
                }
            }
        });

        // 6. Lógica do Botão VER HISTÓRICO
        view.getBtnVerHistorico().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.HistoricoView janelaHistorico = new view.HistoricoView(view);

                for (Transaction t : ledger.getElements()) {
                    String origen = t.getSource().toString().replace("(true)", "").replace("(false)", "").trim();
                    String destino = t.getDestination().toString().replace("(true)", "").replace("(false)", "").trim();
                    String moeda = t.getCoin().toString().replace("BitCoin(BTC)", "BTC").replace("Euro(EUR)", "EUR");
                    String valor = String.valueOf(t.getAmount());

                    // Criptografia em Ação para Auditoria
                    String dadosBrutos = origen + destino + moeda + valor;
                    String hashSeguranca = calcularSHA256(dadosBrutos);

                    janelaHistorico.adicionarLinha(origen, destino, moeda, valor, hashSeguranca);
                }

                janelaHistorico.setVisible(true);
                
                // Evento de Exportação contido dentro da janela de histórico
                janelaHistorico.getBtnExportar().addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent ev) {
                        String ativa = view.getCarteiraAtiva();
                        exportarExtratoParaFicheiro(ativa);
                    }
                });
            }
        });

        // 7. Lógica do Botão NOVA TRANSAÇÃO (Transferência entre contas)
        view.getBtnNovaTransacao().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.NovaTransacaoView janelaNova = new view.NovaTransacaoView(view, carteirasRegistadas);

                janelaNova.getBtnGravar().addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent ev) {
                        String origem = janelaNova.getOrigemSelecionada();
                        String destino = janelaNova.getDestinoSelecionado();
                        String valorTexto = janelaNova.getValor().trim();

                        if (origem == null || destino == null) {
                            JOptionPane.showMessageDialog(janelaNova, "Cria pelo menos duas carteiras primeiro!");
                            return;
                        }
                        if (origem.equals(destino)) {
                            JOptionPane.showMessageDialog(janelaNova, "A carteira de origem e destino não podem ser a mesma!");
                            return;
                        }

                        try {
                            double valor = Double.parseDouble(valorTexto);

                            if (valor <= 0) {
                                JOptionPane.showMessageDialog(janelaNova,
                                        "Erro: O valor da transação tem de ser maior que zero.",
                                        "Valor Inválido", JOptionPane.ERROR_MESSAGE);
                                return;
                            }

                            model.wallet.RegularWallet cartOrigem = new model.wallet.RegularWallet(origem);
                            model.wallet.RegularWallet cartDestino = new model.wallet.RegularWallet(destino);

                            String moedaEscolhida = janelaNova.getMoedaSelecionada();
                            model.coin.Currency moedaObj;

                            if (moedaEscolhida.contains("BTC")) {
                                moedaObj = new model.coin.BitCoin();
                            } else if (moedaEscolhida.contains("ETH")) {
                                moedaObj = new model.coin.Ether();
                            } else {
                                moedaObj = new model.coin.Euro();
                            }

                            Transaction t = new Transaction(cartOrigem, cartDestino, moedaObj, valor);
                            ledger.add(t);

                            calcularEAtualizarSaldo();
                            janelaNova.dispose();
                            JOptionPane.showMessageDialog(view, "Transação gravada com sucesso!");

                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(janelaNova,
                                    "Erro: Escreve um número válido no valor (ex: 2.5)",
                                    "Formato de Número Inválido", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });
                janelaNova.setVisible(true);
            }
        });
    }

    /**
     * O Cérebro do Fluxo: Lê todas as transações do Ledger, filtra as movimentações
     * da carteira atualmente ativa, agrupa os saldos por tipo de moeda e atualiza a View,
     * incluindo a conversão do balanço total consolidado para Euros (€).
     */
    private void calcularEAtualizarSaldo() {
        String ativa = view.getCarteiraAtiva();
        if (ativa == null) return;

        java.util.Map<String, Double> ativosDaConta = new java.util.HashMap<>();
        double totalEur = 0.0;

        for (model.transactions.Transaction t : ledger.getElements()) {
            String origem = t.getSource().toString().replace("(true)", "").replace("(false)", "").trim();
            String destino = t.getDestination().toString().replace("(true)", "").replace("(false)", "").trim();
            String moeda = t.getCoin().toString();
            double valor = t.getAmount();

            if (origem.equals(ativa)) {
                ativosDaConta.put(moeda, ativosDaConta.getOrDefault(moeda, 0.0) - valor);
            }
            if (destino.equals(ativa)) {
                ativosDaConta.put(moeda, ativosDaConta.getOrDefault(moeda, 0.0) + valor);
            }
        }

        view.limparTabela();
        for (String moeda : ativosDaConta.keySet()) {
            double qtd = ativosDaConta.get(moeda);

            model.coin.Currency c = model.coin.CoinFactory.criarMoeda(moeda);
            double valorEur = model.exchange.ConversorMoeda.paraEuro(c, qtd);
            totalEur += valorEur;

            view.adicionarAtivo(moeda, String.format("%.4f", qtd), String.format("%.2f €", valorEur));
        }

        view.atualizarSaldoTotal(String.format("%.2f €", totalEur));
    }

    /**
     * O Motor Criptográfico: Gera um código Hash SHA-256 (Hexadecimal) único 
     * a partir de uma String de dados para simular integridade de auditoria.
     *
     * @param texto Cadeia de caracteres brutos (dados da transação reunidos).
     * @return      A assinatura digital (Hash com 64 caracteres hexadecimais) ou string de erro.
     */
    public static String calcularSHA256(String texto) {
        try {
            java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(texto.getBytes(java.nio.charset.StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception ex) {
            return "Erro ao gerar Hash";
        }
    }

    /**
     * Método auxiliar privado para verificar em tempo real o balanço disponível 
     * de uma moeda específica numa determinada carteira, somando e subtraindo transações.
     *
     * @param carteira   O nome da carteira que se pretende auditar.
     * @param nomeMoeda  O nome/código identificador da moeda (ex: "EUR", "BTC").
     * @return           O saldo líquido atual apurado (double).
     */
    private double obterSaldoDaMoeda(String carteira, String nomeMoeda) {
        double saldo = 0.0;
        for (model.transactions.Transaction t : ledger.getElements()) {
            String origem = t.getSource().toString().replace("(true)", "").replace("(false)", "").trim();
            String destino = t.getDestination().toString().replace("(true)", "").replace("(false)", "").trim();
            String moeda = t.getCoin().toString();
            double valor = t.getAmount();

            if (moeda.equals(nomeMoeda)) {
                if (origem.equals(carteira)) {
                    saldo -= valor; 
                }
                if (destino.equals(carteira)) {
                    saldo += valor; 
                }
            }
        }
        return saldo;
    }

    /**
     * Exporta o extrato bancário detalhado de uma carteira ativa para um ficheiro físico de texto (.txt).
     * Inclui a listagem das operações com os respetivos Hashes SHA-256 calculados na hora.
     *
     * @param carteiraAtiva O nome da carteira da qual se deseja exportar o extrato.
     */
    public void exportarExtratoParaFicheiro(String carteiraAtiva) {
        if (carteiraAtiva == null) return;

        String nomeFicheiro = "extrato_" + carteiraAtiva + ".txt";
        
        try (java.io.BufferedWriter writer = new java.io.BufferedWriter(new java.io.FileWriter(nomeFicheiro))) {
            
            writer.write("==================================================\n");
            writer.write("           EXTRATO BANCÁRIO DIGITAL               \n");
            writer.write("==================================================\n");
            writer.write("Carteira Auditada: " + carteiraAtiva + "\n");
            writer.write("Data de Emissão: " + java.time.LocalDateTime.now().toString() + "\n");
            writer.write("==================================================\n\n");
            writer.write(String.format("%-15s | %-15s | %-10s | %-12s\n", "ORIGEM", "DESTINO", "MOEDA", "VALOR"));
            writer.write("--------------------------------------------------\n");

            for (model.transactions.Transaction t : ledger.getElements()) {
                String origem = t.getSource().toString().replace("(true)", "").replace("(false)", "").trim();
                String destino = t.getDestination().toString().replace("(true)", "").replace("(false)", "").trim();
                String moeda = t.getCoin().toString().replace("BitCoin(BTC)", "BTC").replace("Euro(EUR)", "EUR");
                String valor = String.valueOf(t.getAmount());

                if (origem.equals(carteiraAtiva) || destino.equals(carteiraAtiva)) {
                    writer.write(String.format("%-15s | %-15s | %-10s | %-12s\n", origem, destino, moeda, valor));
                    
                    String dadosBrutos = origem + destino + moeda + valor;
                    String hash = calcularSHA256(dadosBrutos);
                    writer.write("   [HASH SEGURANÇA]: " + hash + "\n");
                    writer.write("--------------------------------------------------\n");
                }
            }

            writer.write("\n==================================================\n");
            writer.write("       FIM DO EXTRATO - SISTEMA CRIPTO WALLET     \n");
            writer.write("==================================================\n");

            JOptionPane.showMessageDialog(view, "Extrato exportado com sucesso para o ficheiro:\n" + nomeFicheiro, 
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);

        } catch (java.io.IOException ex) {
            JOptionPane.showMessageDialog(view, "Erro ao exportar o extrato para ficheiro.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}