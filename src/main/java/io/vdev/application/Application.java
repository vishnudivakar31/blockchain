package io.vdev.application;

import io.vdev.dapp.BlockNode;
import io.vdev.dapp.RestServer;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;

public class Application {
    public static void main(String[] args)
            throws IOException, NoSuchAlgorithmException, InterruptedException, SignatureException, InvalidKeyException, InvalidKeySpecException {
        if(args.length != 2) {
            System.out.println("**Error -- Expected two arguments: port of the node -- port of the rest server");
            return;
        }
        int nodePort = Integer.parseInt(args[0]);
        int serverPort = Integer.parseInt(args[1]);
        BlockNode blockNode = BlockNode.getInstance(nodePort);
        RestServer server = RestServer.getInstance(blockNode);
        server.start(serverPort);
        while (true) {
            Thread.sleep(10000);
            System.out.println("Known peers");
            System.out.println(blockNode.getPeers());
        }
    }
}
