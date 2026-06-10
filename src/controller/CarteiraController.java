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

public class CarteiraController {

    private final CarteiraView view;
    private final CsvLedger ledger;
    private final java.util.List<String> carteirasRegistadas; // Nova lista!

    public CarteiraController(CarteiraView view, model.data.CsvLedger ledger) {
        this.view = view;
        this.ledger = ledger;
        this.carteirasRegistadas = new java.util.ArrayList<>();

        carregarCarteirasDoHistorico();

        // ADICIONAR ESTA LINHA: Injeta os nomes das carteiras no menu pendente
        view.atualizarListaCarteiras(this.carteirasRegistadas);

        inicializarEventos();
        calcularEAtualizarSaldo();
    }
    private void carregarCarteirasDoHistorico() {
        for (model.transactions.Transaction t : ledger.getElements()) {
            String origem = t.getSource().toString().replace("(true)", "").replace("(false)", "").trim();
            String destino = t.getDestination().toString().replace("(true)", "").replace("(false)", "").trim();

            if (!carteirasRegistadas.contains(origem)) carteirasRegistadas.add(origem);
            if (!carteirasRegistadas.contains(destino)) carteirasRegistadas.add(destino);
        }
    }

    private void inicializarEventos() {
        // Quando o botão "Ver Histórico" for clicado
        // 1. Quando o utilizador muda a carteira no menu pendente, a tabela atualiza!
        view.getCbCarteiraAtiva().addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                calcularEAtualizarSaldo();
            }
        });
 // Lógica do Botão DEPOSITAR
        view.getBtnDeposito().addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                String ativa = view.getCarteiraAtiva();
                if (ativa == null) {
                    JOptionPane.showMessageDialog(view, "Seleciona uma carteira primeiro!");
                    return;
                }

                // Mini-formulário rápido dentro de um JOptionPane
                JPanel painelDeposito = new JPanel(new GridLayout(2, 2, 5, 5));
                painelDeposito.add(new JLabel("Moeda:"));
                JComboBox<String> cbMoeda = new JComboBox<>(new String[]{"Euro (EUR)", "Bitcoin (BTC)", "Ethereum (ETH)"});
                painelDeposito.add(cbMoeda);

                painelDeposito.add(new JLabel("Valor a depositar:"));
                JTextField txtValor = new JTextField();
                painelDeposito.add(txtValor);

                // Mostra a janela de depósito
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

                        // Regista o depósito no Ledger
                        ledger.add(new model.transactions.Transaction(origem, destino, moedaObj, valor));

                        // Atualiza a interface
                        calcularEAtualizarSaldo();
                        JOptionPane.showMessageDialog(view, "Depósito de " + valor + " registado com sucesso!");

                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(view, "Valor inválido. Insere apenas números maiores que zero.", "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }); // <-- O DEPOSITAR TERMINA AQUI

// Lógica do Botão LEVANTAR (WITHDRAW)
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

                        // --- VALIDAÇÃO CRÍTICA DE SALDO ---
                        double saldoDisponivel = obterSaldoDaMoeda(ativa, moedaObj.toString());

                        if (valor > saldoDisponivel) {
                            // Lança a exceção personalizada usando o caminho do pacote
                            throw new exceptions.SaldoInsuficienteException(
                                "Saldo insuficiente! Tens apenas " + saldoDisponivel + " " + moedaObj.toString() + " nesta carteira."
                            );
                        }

                        model.wallet.Wallet origem = new model.wallet.RegularWallet(ativa);
                        model.wallet.Wallet destino = new model.wallet.RegularWallet("Levantamento_Externo");

                        ledger.add(new model.transactions.Transaction(origem, destino, moedaObj, -valor));

                        calcularEAtualizarSaldo();
                        JOptionPane.showMessageDialog(view, "Levantamento de " + valor + " registado com sucesso!");

                    } catch (exceptions.SaldoInsuficienteException ex) {
                        // Captura especificamente o teu erro de saldo
                        JOptionPane.showMessageDialog(view, ex.getMessage(), "Erro de Saldo", JOptionPane.ERROR_MESSAGE);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(view, "Valor inválido. Insere apenas números maiores que zero.", "Erro", JOptionPane.ERROR_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(view, "Ocorreu um erro ao processar o levantamento.", "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        
        // Lógica do Botão DEPOSITAR
        view.getBtnDeposito().addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                // ... (toda a lógica do depósito que os teus colegas fizeram) ...
            }
        }); // <-- O DEPOSITAR TERMINA AQUI

        // COLA O BOTÃO DE LEVANTAMENTO EXATAMENTE AQUI:
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

                        // Validação de saldo usando o método auxiliar
                        double saldoDisponivel = obterSaldoDaMoeda(ativa, moedaObj.toString());

                        if (valor > saldoDisponivel) {
                            JOptionPane.showMessageDialog(view, 
                                    "Saldo insuficiente! Tens apenas " + saldoDisponivel + " " + moedaObj.toString() + " nesta carteira.", 
                                    "Erro de Saldo", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        model.wallet.Wallet origem = new model.wallet.RegularWallet(ativa);
                        model.wallet.Wallet destino = new model.wallet.RegularWallet("Levantamento_Externo");

                        // Saída de dinheiro guardada como valor NEGATIVO no histórico
                        ledger.add(new model.transactions.Transaction(origem, destino, moedaObj, -valor));

                        calcularEAtualizarSaldo();
                        JOptionPane.showMessageDialog(view, "Levantamento de " + valor + " registado com sucesso!");

                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(view, "Valor inválido. Insere apenas números maiores que zero.", "Erro", JOptionPane.ERROR_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(view, "Ocorreu um erro ao processar o levantamento.", "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
            
            // Método auxiliar para verificar quanto o utilizador tem de uma moeda específica
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
        }); // <-- FIM DO BOTÃO DE LEVANTAMENTO
        
        
        


        // 2. O botão "Swap (Trocar)"
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

                            // Taxas de Câmbio Fixas (apenas para simulação do projeto)
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
        // Lógica do Botão Criar Carteira (ATUALIZADA PARA A V2.0)
        view.getBtnNovaCarteira().addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                // Abre uma caixinha simples a pedir o nome
                String nomeNovaCarteira = JOptionPane.showInputDialog(view, "Qual o nome da nova carteira?");

                // Valida se o utilizador escreveu algo e se não cancelou
                if (nomeNovaCarteira != null && !nomeNovaCarteira.trim().isEmpty()) {
                    String nomeLimpo = nomeNovaCarteira.trim();

                    if (carteirasRegistadas.contains(nomeLimpo)) {
                        JOptionPane.showMessageDialog(view, "Esta carteira já existe!", "Erro", JOptionPane.ERROR_MESSAGE);
                    } else {
                        // 1. Adiciona a nova carteira à memória do Controller
                        carteirasRegistadas.add(nomeLimpo);

                        // 2. Atualiza o menu pendente (Dropdown) lá no topo da janela!
                        view.atualizarListaCarteiras(carteirasRegistadas);

                        // 3. (Toque de classe UX) Seleciona automaticamente a carteira que acabaste de criar
                        view.getCbCarteiraAtiva().setSelectedItem(nomeLimpo);

                        JOptionPane.showMessageDialog(view, "Carteira '" + nomeLimpo + "' criada com sucesso!");
                    }
                }
            }
        });
        view.getBtnVerHistorico().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.HistoricoView janelaHistorico = new view.HistoricoView(view);

                for (Transaction t : ledger.getElements()) {

                    // 1. Limpeza do "(true)" e "(false)" injetados pelo modelo
                    String origem = t.getSource().toString().replace("(true)", "").replace("(false)", "").trim();
                    String destino = t.getDestination().toString().replace("(true)", "").replace("(false)", "").trim();

                    // (Opcional) Podemos também limpar o nome da moeda para ficar mais elegante na tabela!
                    String moeda = t.getCoin().toString().replace("BitCoin(BTC)", "BTC").replace("Euro(EUR)", "EUR");

                    String valor = String.valueOf(t.getAmount());

                    // 2. Criptografia em Ação
                    String dadosBrutos = origem + destino + moeda + valor;
                    String hashSeguranca = calcularSHA256(dadosBrutos);

                    // 3. Enviar para a grelha
                    janelaHistorico.adicionarLinha(origem, destino, moeda, valor, hashSeguranca);
                }

                janelaHistorico.setVisible(true);
                
                // Lógica para quando clicarem no botão de exportar da janela de histórico
                janelaHistorico.getBtnExportar().addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent ev) {
                        String ativa = view.getCarteiraAtiva();
                        exportarExtratoParaFicheiro(ativa);
                    }
                });
            }
        });

        // Quando o botão "Nova Transação" da janela principal for clicado
        view.getBtnNovaTransacao().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 1. Abre a nova janela de formulário
                // Passamos a lista de carteiras para a janela desenhar os menus pendentes
                view.NovaTransacaoView janelaNova = new view.NovaTransacaoView(view, carteirasRegistadas);

                janelaNova.getBtnGravar().addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent ev) {

                        // Lemos a partir dos menus pendentes!
                        String origem = janelaNova.getOrigemSelecionada();
                        String destino = janelaNova.getDestinoSelecionado();
                        String valorTexto = janelaNova.getValor().trim();

                        // Validação Extra: Não deixar enviar para a própria carteira
                        if (origem == null || destino == null) {
                            JOptionPane.showMessageDialog(janelaNova, "Cria pelo menos duas carteiras primeiro!");
                            return;
                        }
                        if (origem.equals(destino)) {
                            JOptionPane.showMessageDialog(janelaNova, "A carteira de origem e destino não podem ser a mesma!");
                            return;
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
        String ativa = view.getCarteiraAtiva();
        if (ativa == null) return; // Se não houver carteira selecionada, não faz nada

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

            // Assume-se que tens um ConversorMoeda. Se der erro, avisa!
            model.coin.Currency c = model.coin.CoinFactory.criarMoeda(moeda);
            double valorEur = model.exchange.ConversorMoeda.paraEuro(c, qtd);
            totalEur += valorEur;

            view.adicionarAtivo(moeda, String.format("%.4f", qtd), String.format("%.2f €", valorEur));
        }

        view.atualizarSaldoTotal(String.format("%.2f €", totalEur));
    }
    // O MOTOR CRIPTOGRÁFICO: Gera um Hash SHA-256 único para a transação
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
            // Retorna o código hexadecimal completo
            return hexString.toString();
        } catch (Exception ex) {
            return "Erro ao gerar Hash";
        }
    }
    
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
    
    // Método para exportar o extrato bancário em formato TXT
    public void exportarExtratoParaFicheiro(String carteiraAtiva) {
        if (carteiraAtiva == null) return;

        String nomeFicheiro = "extrato_" + carteiraAtiva + ".txt";
        
        // Usamos o BufferedWriter para criar e escrever no ficheiro de texto
        try (java.io.BufferedWriter writer = new java.io.BufferedWriter(new java.io.FileWriter(nomeFicheiro))) {
            
            // Cabeçalho do Extrato
            writer.write("==================================================\n");
            writer.write("           EXTRATO BANCÁRIO DIGITAL               \n");
            writer.write("==================================================\n");
            writer.write("Carteira Auditada: " + carteiraAtiva + "\n");
            writer.write("Data de Emissão: " + java.time.LocalDateTime.now().toString() + "\n");
            writer.write("==================================================\n\n");
            writer.write(String.format("%-15s | %-15s | %-10s | %-12s\n", "ORIGEM", "DESTINO", "MOEDA", "VALOR"));
            writer.write("--------------------------------------------------\n");

            // Percorre o histórico do Ledger
            for (model.transactions.Transaction t : ledger.getElements()) {
                String origem = t.getSource().toString().replace("(true)", "").replace("(false)", "").trim();
                String destino = t.getDestination().toString().replace("(true)", "").replace("(false)", "").trim();
                String moeda = t.getCoin().toString().replace("BitCoin(BTC)", "BTC").replace("Euro(EUR)", "EUR");
                String valor = String.valueOf(t.getAmount());

                // Se a nossa carteira participou nesta transação, adicionamos ao extrato
                if (origem.equals(carteiraAtiva) || destino.equals(carteiraAtiva)) {
                    writer.write(String.format("%-15s | %-15s | %-10s | %-12s\n", origem, destino, moeda, valor));
                    
                    // Adiciona o Hash de segurança logo abaixo da transação para auditoria
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