package io.vdev.dapp;

import io.vdev.util.BlockchainUtils;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class Block {

    private List<Transaction> transactions;
    private String lastHash;
    private String forger;
    private long blockCount;
    private long timestamp;
    private String signature;

    public Block(List<Transaction> transactions, String lastHash, String forger, long blockCount) {
        this.transactions = transactions;
        this.lastHash = lastHash;
        this.forger = forger;
        this.blockCount = blockCount;
        this.timestamp = System.currentTimeMillis();
        this.signature = "";
    }

    private void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public static Block genesis() throws NoSuchAlgorithmException {
        Block genesisBlock = new Block(new ArrayList<>(),
                BlockchainUtils.hash("genesis_block"),
                Wallet.getInstance().getPublicKey(), 0L);
        genesisBlock.setTimestamp(0);
        return genesisBlock;
    }

    public void sign(String signature) {
        this.signature = signature;
    }

    public String payload() {
        return "Block{" +
                "transactions=" + transactions +
                ", lastHash='" + lastHash + '\'' +
                ", forger='" + forger + '\'' +
                ", blockCount=" + blockCount +
                ", timestamp=" + timestamp +
                '}';
    }

    @Override
    public String toString() {
        return "Block{" +
                "transactions=" + transactions +
                ", lastHash='" + lastHash + '\'' +
                ", forger='" + forger + '\'' +
                ", blockCount=" + blockCount +
                ", timestamp=" + timestamp +
                ", signature='" + signature + '\'' +
                '}';
    }
}
