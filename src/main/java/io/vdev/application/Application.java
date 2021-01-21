package io.vdev.application;

import io.vdev.dapp.BlockNode;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class Application {
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, InterruptedException {
        if(args.length != 1) {
            System.out.println("**Error -- Expected one argument: port of the node");
            return;
        }
        int nodePort = Integer.parseInt(args[0]);
        BlockNode blockNode = BlockNode.getInstance(nodePort);
        while (true) {
            Thread.sleep(10000);
            System.out.println("Known peers");
            System.out.println(blockNode.getPeers());
        }
    }
}
