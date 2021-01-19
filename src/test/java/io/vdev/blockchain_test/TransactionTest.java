package io.vdev.blockchain_test;

import io.vdev.dapp.Transaction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Transaction test suite")
public class TransactionTest {

    @Test
    @DisplayName("transaction display and equals test")
    public void transactionTest() {
        Transaction bobTransaction = new Transaction("bob_key",
                "alice_key",
                "TRANSACTION", 1.2);
        Transaction aliceTransaction = new Transaction("bob_key",
                "alice_key",
                "TRANSACTION", 1.2);

        System.out.println(bobTransaction.toString());
        System.out.println("-----------------------------------------------------------------------------");
        System.out.println(bobTransaction.payload());
        System.out.println("-----------------------------------------------------------------------------");
        System.out.println(aliceTransaction.toString());
        System.out.println("-----------------------------------------------------------------------------");
        System.out.println(aliceTransaction.payload());
        System.out.println("-----------------------------------------------------------------------------");
    }
}
