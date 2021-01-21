package io.vdev.dapp;

import io.vdev.util.BlockchainUtils;
import io.vdev.util.Constants;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;

public class Wallet {
    private static Wallet instance = null;
    private static RSAPublicKey publicKey;
    private static RSAPrivateKey privateKey;

    private static final String SIGNATURE_ALGORITHM = "SHA256withRSA";

    private Wallet() {}

    public static Wallet getInstance() throws NoSuchAlgorithmException {
        if(instance == null) {
            instance = new Wallet();
            generateKeys();
        }
        return instance;
    }

    private static void generateKeys() throws NoSuchAlgorithmException {
        File file = new File(Constants.KEYS_DIRECTORY);
        File privateKeyFile = new File(Constants.PRIVATE_KEY_FILE);
        File publicKeyFile = new File(Constants.PUBLIC_KEY_FILE);
        if(file.exists() && privateKeyFile.exists() && publicKeyFile.exists()) {
            privateKey = BlockchainUtils.getPrivateKey(privateKeyFile);
            publicKey = BlockchainUtils.getPublicKey(publicKeyFile);
        } else {
            KeyPair pair = BlockchainUtils.generateKeyPair();
            privateKey = (RSAPrivateKey) pair.getPrivate();
            publicKey = (RSAPublicKey) pair.getPublic();
        }
    }

    public String sign(String dataPayload)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature signatureInstance = Signature.getInstance(SIGNATURE_ALGORITHM);
        signatureInstance.initSign(privateKey);
        signatureInstance.update(dataPayload.getBytes(StandardCharsets.UTF_8));
        byte[] signature = signatureInstance.sign();
        return Base64.getEncoder().encodeToString(signature);
    }

    public static boolean verify(String dataPayload, String signature, RSAPublicKey publicKey)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature signatureInstance = Signature.getInstance(SIGNATURE_ALGORITHM);
        signatureInstance.initVerify(publicKey);
        signatureInstance.update(dataPayload.getBytes(StandardCharsets.UTF_8));

        byte[] signatureBytes = Base64.getDecoder().decode(signature);
        return signatureInstance.verify(signatureBytes);
    }

    public String getPublicKey() {
        String encodedPublicKey = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        return encodedPublicKey;
    }

    public Transaction createTransaction(String receiverPublicKey, String type, Double amount)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Transaction transaction = new Transaction(getPublicKey(), receiverPublicKey, type, amount);
        String signature = sign(transaction.payload());
        transaction.sign(signature);
        return transaction;
    }

}
