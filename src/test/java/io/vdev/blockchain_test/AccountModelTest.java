package io.vdev.blockchain_test;

import io.vdev.dapp.AccountModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Account model test suite")
public class AccountModelTest {

    @Test
    @DisplayName("testing balances")
    public void testBalances() {
        AccountModel accountModel = new AccountModel();
        accountModel.updateBalance("bob", 10D);
        accountModel.updateBalance("alice", 15D);
        accountModel.updateBalance("bob", -5D);
        accountModel.updateBalance("alice", -5D);

        Assertions.assertEquals(accountModel.getBalance("bob"), 5D);
        Assertions.assertEquals(accountModel.getBalance("alice"), 10D);
    }
}
