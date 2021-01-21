package io.vdev.dapp;

import java.util.ArrayList;
import java.util.List;

public class TransactionPool {
    private List<Transaction> transactions = new ArrayList<>();
    private final int poolLimit = 5;

    public void addTransaction(Transaction transaction) {
        if(!transactionExists(transaction))
            transactions.add(transaction);
    }

    public boolean transactionExists(Transaction transaction) {
        for(Transaction poolTransaction : transactions) {
            if(poolTransaction.equals(transaction)) {
                return true;
            }
        }
        return false;
    }

    public int getPoolSize() {
        return transactions.size();
    }

    public void removeFromPool(List<Transaction> approvedTransactions) {
        List<Transaction> newTransactionPool = new ArrayList<>();
        for(Transaction transaction : transactions) {
            boolean insert = true;
            for(Transaction approvedTransaction : approvedTransactions) {
                if(transaction.equals(approvedTransaction)) {
                    insert = false;
                }
            }
            if(insert) {
                newTransactionPool.add(transaction);
            }
        }
        transactions = newTransactionPool;
    }

    public boolean forgeryRequired() {
        return transactions.size() >= poolLimit;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    @Override
    public String toString() {
        return "TransactionPool{" +
                "transactions=" + transactions +
                ", poolLimit=" + poolLimit +
                ", size=" + transactions.size() +
                '}';
    }
}
