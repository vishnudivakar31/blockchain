package io.vdev.application;

import io.vdev.dapp.BlockNode;
import io.vdev.dapp.Transaction;
import io.vdev.dapp.Wallet;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;

public class Application {
    public static void main(String[] args)
            throws IOException, NoSuchAlgorithmException, InterruptedException, SignatureException, InvalidKeyException, InvalidKeySpecException {
        if(args.length != 1) {
            System.out.println("**Error -- Expected one argument: port of the node");
            return;
        }
        int nodePort = Integer.parseInt(args[0]);
        BlockNode blockNode = BlockNode.getInstance(nodePort);
        if(blockNode.getCurrentIPPort().equals(blockNode.getKnownIpPort())) {
            Wallet bobWallet = Wallet.getInstance();
            Transaction bobTransaction = blockNode.getWallet().createTransaction(bobWallet.getPublicKey(), "TRANSFER", 10.2);
            Transaction aliceTransaction = blockNode.getWallet().createTransaction(bobWallet.getPublicKey(), "TRANSFER", 11.2);
            blockNode.addToTransactionPool(bobTransaction);
            blockNode.addToTransactionPool(aliceTransaction);
        }
        while (true) {
            Thread.sleep(10000);
            System.out.println("Known peers");
            System.out.println(blockNode.getPeers());
            System.out.println("-------------------------------------");
            System.out.println("Transaction Pool -- Size: " + blockNode.getPool().getPoolSize());
            System.out.println(blockNode.getPool());
        }
    }
}
