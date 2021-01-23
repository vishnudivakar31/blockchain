package io.vdev.dapp;

import java.util.HashMap;
import java.util.Map;

public class AccountModel {
    private Map<String, Double> balanceLedger;

    public AccountModel() {
        balanceLedger = new HashMap<>();
    }

    public void addAccount(String publicKey) {
        if(!balanceLedger.containsKey(publicKey)) {
            balanceLedger.put(publicKey, 0D);
        }
    }

    public Double getBalance(String publicKey) {
        if(!balanceLedger.containsKey(publicKey)) {
            addAccount(publicKey);
        }
        return balanceLedger.get(publicKey);
    }

    public void updateBalance(String publicKey, Double amount) {
        if(!balanceLedger.containsKey(publicKey)) {
            addAccount(publicKey);
        }
        Double currentBalance = getBalance(publicKey);
        balanceLedger.put(publicKey, currentBalance + amount);
    }

}
