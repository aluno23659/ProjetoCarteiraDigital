package application;

import model.coin.BitCoin;
import model.data.CsvLedger;
import model.transactions.Transaction;
import model.wallet.RegularWallet;

public class Main {

    public static void main(String[] args) {
        System.out.println("--- A INICIAR O TESTE DO MOTOR DE DADOS ---");

        // 1. Iniciar o Ledger
        // Como já criaste o método carregarTransacoes(), se o ficheiro já existir de testes anteriores,
        // ele vai carregar esse histórico logo aqui!
        CsvLedger ledger = new CsvLedger("historico_transacoes.csv");

        // 2. Criar as carteiras (O teu professor definiu que o construtor recebe uma String 'adress')
        RegularWallet carteiraJoao = new RegularWallet("Endereco_Joao_123");
        RegularWallet carteiraMaria = new RegularWallet("Endereco_Maria_456");

        // 3. Criar a moeda de teste
        BitCoin btc = new BitCoin();

        // 4. Criar a transação
        // ATENÇÃO: Se esta linha ficar a vermelho, significa que o construtor da 'Transaction'
        // do professor pede os dados por outra ordem (ex: Valor, Moeda, Origem, Destino).
        // É só trocares a ordem aqui dentro dos parênteses até o vermelho desaparecer!
        Transaction transacaoTeste = new Transaction(carteiraJoao, carteiraMaria, btc, 2.5);

        // 5. Enviar para o Ledger (Isto vai disparar o teu método gravarTransacao!)
        ledger.add(transacaoTeste);

        System.out.println("Transação guardada com sucesso!");
        System.out.println("Verifica se o ficheiro 'historico_transacoes.csv' apareceu no teu projeto.");

        // ... (o teu código anterior do CsvLedger)

        System.out.println("\n--- INICIAR O TESTE DO MOTOR DE CÂMBIOS ---");

        // 1. Instanciar as moedas
        BitCoin moedaBtc = new BitCoin();
        model.coin.Euro moedaEuro = new model.coin.Euro(); // O IntelliJ pode pedir Alt+Enter para importar

        // 2. Testar a conversão direta para Euro (ex: vender 2.5 BTC)
        double saldoEmEuros = model.exchange.ConversorMoeda.paraEuro(moedaBtc, 2.5);
        System.out.println("Vender 2.5 " + moedaBtc.getSymbol() + " dá: " + saldoEmEuros + " EUR");

        // 3. Testar a conversão entre moedas (ex: comprar BTC com 120.000 Euros)
        double compraBtc = model.exchange.ConversorMoeda.converter(moedaEuro, moedaBtc, 120000.0);
        System.out.println("Com 120000 EUR consegues comprar: " + compraBtc + " " + moedaBtc.getSymbol());
    }
}