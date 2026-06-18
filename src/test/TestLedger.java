package test;

import java.util.ArrayList;
import model.coin.BitCoin;
import model.coin.Currency;
import model.data.Ledger;
import model.data.MemoryLedger;
import model.transactions.Transaction;
import model.wallet.RegularWallet;
import model.wallet.SystemWallet;
import model.wallet.Wallet;

/**
 * Classe de teste responsável por validar o comportamento do livro de registos (Ledger).
 * <p>
 * Este ambiente de execução simulada (ponto de entrada {@code main}) cria instâncias de
 * carteiras do sistema e regulares, popula o repositório {@link MemoryLedger} com 
 * múltiplas transações e testa o algoritmo de filtragem por utilizador específico.
 * </p>
 *
 * @author Seu Nome ou Organização
 * @version 1.0
 */
public class TestLedger {

    /**
     * Identificador de versão único para o processo de serialização.
     * <p>
     * Mantém o padrão estrutural e a assinatura de boas práticas definida para 
     * todas as classes do ecossistema do projeto.
     * </p>
     */
    private static final long serialVersionUID = 202605261818L;

    /**
     * Ponto de entrada principal da aplicação de teste para o Ledger.
     * <p>
     * O fluxo de execução realiza as seguintes operações ordenadas:
     * </p>
     * <ol>
     * <li>Inicializa um livro de registos volátil em memória RAM.</li>
     * <li>Instancia uma carteira central de sistema e três carteiras de utilizadores regulares.</li>
     * <li>Regista transferências de Bitcoin, simulando transações válidas e cenários de teste.</li>
     * <li>Filtra e isola o histórico de transações de um utilizador específico ("maria").</li>
     * <li>Imprime o resultado formatado na consola.</li>
     * </ol>
     *
     * @param args Argumentos da linha de comandos passados na execução (não utilizados).
     */
    public static void main(String[] args) {
        // Inicialização do livro de registos em memória
        Ledger mySystem = new MemoryLedger();

        // Criação das entidades de teste (carteiras e moeda)
        Wallet s1 = new SystemWallet();
        Wallet r2 = new RegularWallet("ana");
        Wallet r3 = new RegularWallet("maria");
        Wallet r4 = new RegularWallet("joao");
        Currency bitcoin = new BitCoin();

        // Simulação do fluxo de transações
        mySystem.add(new Transaction(s1, r2, bitcoin, 100));
        mySystem.add(new Transaction(s1, r3, bitcoin, 100));
        
        // Simulação de transação para fins de validação no histórico
        mySystem.add(new Transaction(r4, r3, bitcoin, 10));

        // Filtragem do histórico focado na carteira da "maria"
        ArrayList<Transaction> maria = mySystem.filter(r3);
        
        // Exibição dos registos capturados pelo filtro
        for (Transaction transaction : maria) {
            System.out.println(transaction);
        }
    }
}