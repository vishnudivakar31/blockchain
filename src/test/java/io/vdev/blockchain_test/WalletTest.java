package io.vdev.blockchain_test;

import io.vdev.dapp.Transaction;
import io.vdev.dapp.Wallet;
import io.vdev.util.BlockchainUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;

@DisplayName("Wallet test suite")
public class WalletTest {

    @Test
    @DisplayName("signature verify test")
    public void testWallet() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, InvalidKeySpecException {
        Wallet wallet = Wallet.getInstance();
        Transaction transaction = wallet.createTransaction("receiver", "TRANSACTION", 10D);
        String signature = transaction.getSignature();
        Assertions.assertTrue(Wallet.verify(transaction.payload(), signature, BlockchainUtils.getPublicKeyFromString(wallet.getPublicKey())));
    }

    @Test
    @DisplayName("unique transaction creation test")
    public void testUniqueTransactionCreation() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        Wallet wallet = Wallet.getInstance();
        Transaction transaction = wallet.createTransaction("receiver", "TRANSACTION", 10D);
        Transaction anotherTransaction = wallet.createTransaction("receiver", "TRANSACTION", 10D);
        System.out.println(transaction.toString());
        System.out.println(anotherTransaction.toString());
        Assertions.assertFalse(transaction.equals(anotherTransaction));
    }
}
