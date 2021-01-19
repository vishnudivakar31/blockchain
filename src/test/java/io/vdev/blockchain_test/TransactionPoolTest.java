package io.vdev.blockchain_test;

import io.vdev.dapp.Transaction;
import io.vdev.dapp.TransactionPool;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Transaction Pool Test Suite")
public class TransactionPoolTest {
    @Test
    @DisplayName("transaction pool size test")
    public void transactionPoolSizeTest() {
        Transaction bobTransaction = new Transaction("bob_key",
                "alice_key",
                "TRANSACTION", 1.2);
        bobTransaction.sign("bob");
        Transaction aliceTransaction = new Transaction("bob_key",
                "alice_key",
                "TRANSACTION", 1.2);
        aliceTransaction.sign("alice");
        TransactionPool pool = new TransactionPool();
        pool.addTransaction(bobTransaction);
        pool.addTransaction(aliceTransaction);
        pool.addTransaction(aliceTransaction);
        System.out.println(pool.toString());
        Assertions.assertEquals(pool.getTransactions().size(),2);
    }

    @Test
    @DisplayName("transaction pool forger false test")
    public void transactionPoolForgerFalseTest() {
        Transaction bobTransaction = new Transaction("bob_key",
                "alice_key",
                "TRANSACTION", 1.2);
        bobTransaction.sign("bob");
        Transaction aliceTransaction = new Transaction("bob_key",
                "alice_key",
                "TRANSACTION", 1.2);
        aliceTransaction.sign("alice");
        TransactionPool pool = new TransactionPool();
        pool.addTransaction(bobTransaction);
        pool.addTransaction(aliceTransaction);
        pool.addTransaction(aliceTransaction);
        Assertions.assertFalse(pool.forgeryRequired());
    }

    @Test
    @DisplayName("transaction pool forger true test")
    public void transactionPoolForgerTest() {
        TransactionPool pool = new TransactionPool();
        for(int i = 0; i < 5; i++) {
            Transaction bobTransaction = new Transaction("bob_key",
                    "alice_key",
                    "TRANSACTION", 1.2);
            bobTransaction.sign("bob");
            pool.addTransaction(bobTransaction);
        }
        Assertions.assertTrue(pool.forgeryRequired());
    }
}
