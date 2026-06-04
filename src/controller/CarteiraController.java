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
    private java.util.List<String> carteirasRegistadas; // Nova lista!

    public CarteiraController(CarteiraView view, CsvLedger ledger) {
        this.view = view;
        this.ledger = ledger;
        this.carteirasRegistadas = new java.util.ArrayList<>();

        // Vai buscar as carteiras que já existem no histórico
        carregarCarteirasDoHistorico();

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
        // Quando o botão "Ver Histórico" for clicado
        // NOVO: Lógica do Botão Criar Carteira
        view.getBtnNovaCarteira().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Abre uma caixinha simples a pedir o nome
                String nomeNovaCarteira = JOptionPane.showInputDialog(view, "Qual o nome da nova carteira?");

                // Valida se o utilizador escreveu algo e se não cancelou
                if (nomeNovaCarteira != null && !nomeNovaCarteira.trim().isEmpty()) {
                    String nomeLimpo = nomeNovaCarteira.trim();

                    if (carteirasRegistadas.contains(nomeLimpo)) {
                        JOptionPane.showMessageDialog(view, "Esta carteira já existe!", "Erro", JOptionPane.ERROR_MESSAGE);
                    } else {
                        carteirasRegistadas.add(nomeLimpo);
                        // Atualiza a tabela com a nova carteira a zeros
                        view.adicionarCarteiraTabela(nomeLimpo, "0.0000", "-");
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
        // Agora a chave vai ser composta: "NomeDaCarteira||Moeda" para não misturar dinheiros diferentes!
        java.util.Map<String, Double> saldosPorCarteira = new java.util.HashMap<>();
        double saldoTotalGeralEuros = 0.0;

        for (Transaction t : ledger.getElements()) {
            // 1. Limpar o "(true)" ou "(false)" que o Java injeta através do toString() do Modelo
            String origem = t.getSource().toString().replace("(true)", "").replace("(false)", "").trim();
            String destino = t.getDestination().toString().replace("(true)", "").replace("(false)", "").trim();

            double valor = t.getAmount();
            String nomeMoeda = t.getCoin().toString(); // Vai buscar o nome real da moeda da transação

            // 2. Criar chaves únicas para cada tipo de moeda na mesma carteira
            String chaveOrigem = origem + "||" + nomeMoeda;
            String chaveDestino = destino + "||" + nomeMoeda;

            // 3. Atualizar saldos subtraindo à origem e somando ao destino
            saldosPorCarteira.put(chaveOrigem, saldosPorCarteira.getOrDefault(chaveOrigem, 0.0) - valor);
            saldosPorCarteira.put(chaveDestino, saldosPorCarteira.getOrDefault(chaveDestino, 0.0) + valor);

            // 4. O saldo geral continua a ser consolidado em Euros usando o teu Conversor
            saldoTotalGeralEuros += ConversorMoeda.paraEuro(t.getCoin(), valor);
        }

        view.limparTabela();

        for (String chave : saldosPorCarteira.keySet()) {
            double saldoFinal = saldosPorCarteira.get(chave);

            // Separar a nossa chave composta para voltar a ter o Nome e a Moeda
            String[] partes = chave.split("\\|\\|");
            String nomeCarteira = partes[0];
            String moeda = partes[1];

            // Injeta na View com a Moeda correta!
            view.adicionarCarteiraTabela(nomeCarteira, String.format("%.4f", saldoFinal), moeda);
        }

        view.atualizarSaldoGeral("Total Consolidado: " + String.format("%.2f", saldoTotalGeralEuros) + " EUR");
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
}
