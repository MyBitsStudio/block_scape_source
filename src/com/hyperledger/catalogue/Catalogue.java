package com.hyperledger.catalogue;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Catalogue {

    private static Catalogue singleton;

    public static Catalogue singleton(){
        if(singleton == null){
            singleton = new Catalogue();
        }
        return singleton;
    }

    private final Map<String, String[]> transactions = new ConcurrentHashMap<>();
    private final Map<String, String[]> blocks = new ConcurrentHashMap<>();
    private final Map<String, String[]> wallets = new ConcurrentHashMap<>();
    private final Map<String, String[]> contracts = new ConcurrentHashMap<>();

    public void addTransaction(String id, String[] transaction){
        transactions.put(id, transaction);
    }

    public void addBlock(String id, String[] block){
        blocks.put(id, block);
    }

    public void addWallet(String id, String[] wallet){
        wallets.put(id, wallet);
    }

    public void addContract(String id, String[] contract){
        contracts.put(id, contract);
    }

    public String[] getTransaction(String id){
        return transactions.get(id);
    }

    public String[] getBlock(String id){
        return blocks.get(id);
    }

    public String[] getWallet(String id){
        return wallets.get(id);
    }

    public String[] getContract(String id){
        return contracts.get(id);
    }

    public void replaceTransaction(String id, String[] transaction){
        transactions.replace(id, transaction);
    }

    public void replaceBlock(String id, String[] block){
        blocks.replace(id, block);
    }

    public void replaceWallet(String id, String[] wallet){
        wallets.replace(id, wallet);
    }

    public void replaceContract(String id, String[] contract){
        contracts.replace(id, contract);
    }

}
