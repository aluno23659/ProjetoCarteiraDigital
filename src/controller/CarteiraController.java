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
        // Quando o botão "Ver Histórico" for clicado
        // Quando o botão "Ver Histórico" for clicado
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
