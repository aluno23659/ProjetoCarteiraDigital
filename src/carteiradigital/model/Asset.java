/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package carteiradigital.model;

import java.math.BigDecimal;

/**
 *
 * @author JUNIN
 */
public class Asset {
    
    private final String name; // Bit, Euro .. 
    private final String symbol; //BTC ou EUR
    private BigDecimal amount; // Quantidade (sempre usar BigDecimal para dinheiro)
    
    
    public Asset(String name, String symbol, BigDecimal amount ){
            this.name = name;
            this.symbol = symbol;
            this.amount = amount;
    }
    
    
    //Getters e Setters
    public BigDecimal getAmount() {
        return amount ;
    }
    
    public String getSymbol (){
        return symbol;
    }
    
    //Essa classe permitira que possamos ter varios tipos
    //de moedas. 
}
