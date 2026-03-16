package carteiradigital.model;

import carteiradigital.util.HashUtil;
import java.util.ArrayList;
import java.util.List;

public class Wallet {
    
    private String address; //Endereço único (Hash)
    private String owner; //Nome do dono
    private List<Asset> assets; //Lista do que a carteura tem (BTC, EUR, ETC.)

    public Wallet (String owner){
        
        this.owner = owner;
        this.address= address;
        this.assets = new ArrayList<>();
        String dadosParaHash = owner + System.currentTimeMillis();
        this.address = HashUtil.applySha256(owner + System.currentTimeMillis());
    }
    
    //Métodos para gerir ativos
    public void addAsset (Asset asset){
        this.assets.add(asset);
    }
    
    //Getters
    public String getAddress () { 
            return address;
    }
    
    public String getOwner () {
        return owner;
    }
    
    public List<Asset> getAssets() {
        return assets;
    
}
    
    
    //O address da classe sera a base do blockchain. 
    







}