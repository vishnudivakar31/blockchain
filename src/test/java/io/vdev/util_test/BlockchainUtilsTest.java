package io.vdev.util_test;

import io.vdev.dapp.util.BlockchainUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@DisplayName("blockchain utils test suite")
public class BlockchainUtilsTest {

    @Test
    @DisplayName("key pair validation test")
    void validateKeyPairs() throws NoSuchAlgorithmException, IOException {
        KeyPair pair = BlockchainUtils.generateKeyPair();
        RSAPrivateKey originalPrivateKey = (RSAPrivateKey) pair.getPrivate();
        RSAPublicKey originalPublicKey = (RSAPublicKey) pair.getPublic();
        BlockchainUtils.generatePemFile(pair);
        
        RSAPrivateKey privateKey = BlockchainUtils.getPrivateKey(new File(System.getProperty("user.dir") + "/id_rsa"));
        RSAPublicKey publicKey = BlockchainUtils.getPublicKey(new File(System.getProperty("user.dir") + "/id_rsa_pub"));

        Assertions.assertEquals(originalPrivateKey, privateKey);
        Assertions.assertEquals(originalPublicKey, publicKey);
    }

    @Test
    @DisplayName("hashing validation test")
    void validateHashing() throws NoSuchAlgorithmException {
        String value1 = "how are you";
        String value2 = "how are you";
        Assertions.assertEquals(BlockchainUtils.hash(value1), BlockchainUtils.hash(value2));
    }
}
