package io.vdev.dapp;

import java.util.UUID;

public class Transaction {
    private String senderPublicKey;
    private String receiverPublicKey;
    private String type;
    private Double amount;
    private String id;
    private long timestamp;
    private String signature;

    public Transaction(String senderPublicKey, String receiverPublicKey, String type, Double amount) {
        this.senderPublicKey = senderPublicKey;
        this.receiverPublicKey = receiverPublicKey;
        this.type = type;
        this.amount = amount;
        this.id = UUID.randomUUID().toString();
        this.timestamp = System.currentTimeMillis();
    }

    public void sign(String signature) {
        this.signature = signature;
    }

    public boolean equals(Transaction transaction) {
        return this.id.equals(transaction.getId()) && this.signature.equals(transaction.getSignature());
    }

    public String getSenderPublicKey() {
        return senderPublicKey;
    }

    public String getReceiverPublicKey() {
        return receiverPublicKey;
    }

    public String getType() {
        return type;
    }

    public Double getAmount() {
        return amount;
    }

    public String getId() {
        return id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getSignature() {
        return signature;
    }

    public String payload() {
        return "Transaction{" +
                "senderPublicKey='" + senderPublicKey + '\'' +
                ", receiverPublicKey='" + receiverPublicKey + '\'' +
                ", type='" + type + '\'' +
                ", amount=" + amount +
                ", id='" + id + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "senderPublicKey='" + senderPublicKey + '\'' +
                ", receiverPublicKey='" + receiverPublicKey + '\'' +
                ", type='" + type + '\'' +
                ", amount=" + amount +
                ", id='" + id + '\'' +
                ", timestamp=" + timestamp +
                ", signature='" + signature + '\'' +
                '}';
    }
}
