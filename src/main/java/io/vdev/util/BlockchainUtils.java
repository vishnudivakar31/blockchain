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

    public static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048, new SecureRandom());
        return generator.genKeyPair();
    }

    public static void generatePemFile(KeyPair pair) throws IOException {
        File keysDirectory = new File("keys");
        if(!keysDirectory.exists())
            keysDirectory.mkdir();

        RSAPrivateKey privateKey = (RSAPrivateKey) pair.getPrivate();
        PemFile pemFile = new PemFile(privateKey, "RSA PRIVATE KEY");
        pemFile.write("keys/id_rsa");

        RSAPublicKey publicKey = (RSAPublicKey) pair.getPublic();
        PemFile publicPem = new PemFile(publicKey, "RSA PUBLIC KEY");
        publicPem.write("keys/id_rsa_pub");
    }

    public static RSAPrivateKey getPrivateKey(File file) throws NoSuchAlgorithmException {
        KeyFactory factory = KeyFactory.getInstance("RSA");

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
        KeyFactory factory = KeyFactory.getInstance("RSA");

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
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(originalString.getBytes(StandardCharsets.UTF_8));
        return new String(Hex.encode(hash));
    }

}
