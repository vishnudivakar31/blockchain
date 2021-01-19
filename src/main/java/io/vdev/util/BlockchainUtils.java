package io.vdev.util;

import org.bouncycastle.util.encoders.Hex;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class BlockchainUtils {

    private static final String ENCRYPTION_ALGORITHM = "RSA";
    private static final String HASHING_ALGORITHM = "SHA-256";
    private static final Integer KEY_SIZE = 2048;

    public static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance(ENCRYPTION_ALGORITHM);
        generator.initialize(KEY_SIZE, new SecureRandom());
        return generator.genKeyPair();
    }

    public static void generatePemFile(KeyPair pair) throws IOException {
        File keysDirectory = new File(Constants.KEYS_DIRECTORY);
        if(!keysDirectory.exists())
            keysDirectory.mkdir();

        RSAPrivateKey privateKey = (RSAPrivateKey) pair.getPrivate();
        PemFile pemFile = new PemFile(privateKey, "RSA PRIVATE KEY");
        pemFile.write(Constants.PRIVATE_KEY_FILE);

        RSAPublicKey publicKey = (RSAPublicKey) pair.getPublic();
        PemFile publicPem = new PemFile(publicKey, "RSA PUBLIC KEY");
        publicPem.write(Constants.PUBLIC_KEY_FILE);
    }

    public static RSAPrivateKey getPrivateKey(File file) throws NoSuchAlgorithmException {
        KeyFactory factory = KeyFactory.getInstance(ENCRYPTION_ALGORITHM);

        try (FileReader keyReader = new FileReader(file); PemReader pemReader = new PemReader(keyReader)) {
            PemObject pemObject = pemReader.readPemObject();
            byte[] content = pemObject.getContent();
            PKCS8EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(content);
            return (RSAPrivateKey) factory.generatePrivate(privKeySpec);
        } catch (IOException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static RSAPublicKey getPublicKey(File file) throws NoSuchAlgorithmException {
        KeyFactory factory = KeyFactory.getInstance(ENCRYPTION_ALGORITHM);

        try (FileReader keyReader = new FileReader(file); PemReader pemReader = new PemReader(keyReader)) {
            PemObject pemObject = pemReader.readPemObject();
            byte[] content = pemObject.getContent();
            X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(content);
            return (RSAPublicKey) factory.generatePublic(pubKeySpec);
        } catch (InvalidKeySpecException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String hash(String originalString) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(HASHING_ALGORITHM);
        byte[] hash = digest.digest(originalString.getBytes(StandardCharsets.UTF_8));
        return new String(Hex.encode(hash));
    }

}
