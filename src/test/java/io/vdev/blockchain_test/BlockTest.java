package io.vdev.blockchain_test;

import io.vdev.dapp.Block;
import io.vdev.dapp.Transaction;
import io.vdev.dapp.Wallet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.List;

@DisplayName("Block test suite")
public class BlockTest {

    @Test
    @DisplayName("display block")
    public void displayBlock() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        Wallet wallet = Wallet.getInstance();
        Transaction bobTransaction = wallet.createTransaction(wallet.getPublicKey(), "TRANSFER", 10.0);
        Transaction alice = wallet.createTransaction(wallet.getPublicKey(), "TRANSFER", 12.0);
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(bobTransaction);
        transactions.add(alice);
        Block block = wallet.createBlock(transactions, "last_hash", 0);
        System.out.println(block.toString());
    }
}
