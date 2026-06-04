package model.data;

import model.transactions.Transaction;
import model.wallet.Wallet;
import model.coin.CoinFactory; // Importamos a nossa fábrica
import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CsvLedger extends Ledger {

    private final String filePath;
    private ArrayList<Transaction> data;

    public CsvLedger(String filePath) {
        this.filePath = filePath;
        this.data = new ArrayList<>();
        carregarTransacoes();
    }

    @Override
    public void add(Transaction t) {
        data.add(t);
        gravarTransacao(t);
    }

    @Override
    public ArrayList<Transaction> filter(Wallet w) {
        ArrayList<Transaction> userData = new ArrayList<>();
        for (Transaction transaction : data) {
            if (transaction.getSource().equals(w) || transaction.getDestination().equals(w)) {
                userData.add(transaction);
            }
        }
        return userData;
    }

    private void gravarTransacao(Transaction t) {
        try (FileWriter writer = new FileWriter(this.filePath, true)) {

            // CORREÇÃO: Agora guardamos também o nome da moeda como a 3ª coluna!
            String linhaCSV = t.getSource().getAddress() + ";" +
                    t.getDestination().getAddress() + ";" +
                    t.getCoin().toString() + ";" +  // <--- Grava a moeda real (BTC, ETH, EUR...)
                    t.getAmount() + ";" +
                    System.currentTimeMillis() + "\n";

            writer.write(linhaCSV);

        } catch (IOException e) {
            System.out.println("ERRO CRÍTICO: Não foi possível gravar a transação no ficheiro.");
            System.out.println(e.getMessage());
        }
    }

    private void carregarTransacoes() {
        java.io.File ficheiro = new java.io.File(this.filePath);

        if (!ficheiro.exists()) {
            return;
        }

        try (java.util.Scanner scanner = new java.util.Scanner(ficheiro)) {
            while (scanner.hasNextLine()) {
                String linha = scanner.nextLine();
                String[] partes = linha.split(";"); // [Origem, Destino, Moeda, Valor, Data]

                // CORREÇÃO: Agora garantimos que a linha tem pelo menos 4 elementos
                if (partes.length >= 4) {
                    String origemStr = partes[0];
                    String destinoStr = partes[1];
                    String moedaStr = partes[2];   // <--- Lemos o texto da moeda
                    double valor = Double.parseDouble(partes[3]); // <--- O valor passou para o índice 3

                    Wallet origem = new model.wallet.RegularWallet(origemStr);
                    Wallet destino = new model.wallet.RegularWallet(destinoStr);

                    // CORREÇÃO: Usamos a Fábrica para obter o objeto da moeda correto!
                    model.coin.Currency moedaObj = CoinFactory.criarMoeda(moedaStr);

                    // Criamos a transação com a moeda dinâmica recuperada do CSV
                    Transaction t = new Transaction(origem, destino, moedaObj, valor);

                    this.data.add(t);
                }
            }
            System.out.println("BACKUP: Foram carregadas " + this.data.size() + " transações do ficheiro!");
        } catch (Exception e) {
            System.out.println("Erro ao carregar o backup: " + e.getMessage());
        }
    }

    public java.util.ArrayList<model.transactions.Transaction> getElements() {
        return this.data;
    }
}