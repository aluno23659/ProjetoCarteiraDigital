package model.data;

import model.transactions.Transaction;
import model.wallet.Wallet;
import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class CsvLedger extends Ledger {

    // Caminho onde o ficheiro vai ser guardado
    private final String filePath;

    // Lista para manter as transações em memória enquanto o programa corre
    private ArrayList<Transaction> data;

    // Construtor
    public CsvLedger(String filePath) {
        this.filePath = filePath;
        this.data = new ArrayList<>();
        // Futuramente: chamar método para carregar dados do ficheiro ao iniciar
        carregarTransacoes();
    }

    @Override
    public void add(Transaction t) {
        // 1. Adiciona a transação à lista em memória (para a interface gráfica usar)
        data.add(t);

        // 2. Grava imediatamente a transação no ficheiro CSV
        gravarTransacao(t);
    }

    @Override
    public ArrayList<Transaction> filter(Wallet w) {
        // Lógica para filtrar transações de uma carteira específica
        ArrayList<Transaction> userData = new ArrayList<>();
        for (Transaction transaction : data) {
            // Assumindo que a tua Transaction tem métodos getSource() e getDestination()
            if (transaction.getSource().equals(w) || transaction.getDestination().equals(w)) {
                userData.add(transaction);
            }
        }
        return userData;
    }

    // Método privado (encapsulamento) exclusivo desta classe
    private void gravarTransacao(Transaction t) {
        // O parâmetro 'true' no FileWriter significa "Append" (adicionar ao fim do ficheiro)
        // Se fosse 'false' ou não existisse, ele apagava o ficheiro todo e escrevia por cima.
        try (FileWriter writer = new FileWriter(this.filePath, true)) {

            // Formatamos os dados num formato CSV simples: Origem;Destino;Valor;Data
            String linhaCSV = t.getSource().getAddress() + ";" +
                    t.getDestination().getAddress() + ";" +
                    t.getAmount() + ";" +
                    System.currentTimeMillis() + "\n";



            writer.write(linhaCSV);

        } catch (IOException e) {
            // Se algo correr mal, o programa não estoira, apenas imprime o erro na consola.
            System.out.println("ERRO CRÍTICO: Não foi possível gravar a transação no ficheiro.");
            System.out.println(e.getMessage());
        }
    }
    
    // Método para ler o ficheiro quando o programa inicia
    private void carregarTransacoes() {
        java.io.File ficheiro = new java.io.File(this.filePath);

        // Se for a primeira vez que corremos o programa, o ficheiro não existe. Saimos do método.
        if (!ficheiro.exists()) {
            return;
        }

        try (java.util.Scanner scanner = new java.util.Scanner(ficheiro)) {
            while (scanner.hasNextLine()) {
                String linha = scanner.nextLine();
                String[] partes = linha.split(";"); // Corta o texto: [Origem, Destino, Valor, Data]

                if (partes.length >= 3) { // Garantir que a linha tem dados suficientes
                    String origemStr = partes[0];
                    String destinoStr = partes[1];
                    double valor = Double.parseDouble(partes[2]);

                    // Temos de recriar os objetos a partir do texto
                    Wallet origem = new model.wallet.RegularWallet(origemStr);
                    Wallet destino = new model.wallet.RegularWallet(destinoStr);

                    // Nota: Como não guardaste o tipo de moeda no CSV, assumimos BitCoin para o backup.
                    Transaction t = new Transaction(origem, destino, new model.coin.BitCoin(), valor);

                    // Adicionamos diretamente à lista 'data'.
                    // NÃO usamos o this.add(t) aqui, senão ele ia gravar tudo de novo no ficheiro!
                    this.data.add(t);
                }
            }
            System.out.println("BACKUP: Foram carregadas " + this.data.size() + " transações do ficheiro!");
        } catch (Exception e) {
            System.out.println("Erro ao carregar o backup: " + e.getMessage());
        }
    }
    // Método para permitir que a interface saiba quantas transações existem
    public java.util.ArrayList<model.transactions.Transaction> getElements() {
        return this.data;
    }
}