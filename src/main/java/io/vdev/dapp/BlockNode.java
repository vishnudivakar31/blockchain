package io.vdev.dapp;

import io.vdev.socket.Message;
import io.vdev.socket.Node;
import io.vdev.socket.NodeListener;
import io.vdev.socket.Sender;
import io.vdev.util.Constants;
import io.vdev.util.P2PUtil;

import java.io.IOException;
import java.net.InetAddress;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class BlockNode implements NodeListener {
    private static BlockNode instance = null;
    private final Node node;
    private final TransactionPool pool;
    private final Wallet wallet;
    private List<Peer> peers;
    private final int port;

    private BlockNode(int port) throws NoSuchAlgorithmException, IOException {
        this.node = new Node(port, this);
        this.pool = new TransactionPool();
        this.wallet = Wallet.getInstance();
        this.peers = new ArrayList<>();
        this.port = port;
        System.out.println("node started on " + port + " epoch: " + System.currentTimeMillis());
        if(!InetAddress.getLocalHost().getHostAddress().equals(Constants.KNOWN_HOST_IP) && (port != Constants.KNOWN_HOST_PORT)) {
            System.out.println("discovering nodes...");
            BlockMessage blockMessage = new BlockMessage(Constants.MessageConstants.NEW_CONNECTION, "");
            Message msg = new Message(new Sender(Constants.KNOWN_HOST_IP, Constants.KNOWN_HOST_PORT),
                    P2PUtil.convertToByteArray(blockMessage));
            this.node.sendToNode(msg);
        }
    }

    public static BlockNode getInstance(int port) throws NoSuchAlgorithmException, IOException {
        if(instance == null) {
            instance = new BlockNode(port);
        }
        return instance;
    }

    private void broadcast(BlockMessage message) throws IOException {
        for(Peer peer : peers) {
            Sender sender = new Sender(peer.getIp(), peer.getPort());
            Message msg = new Message(sender, P2PUtil.convertToByteArray(message));
            this.node.sendToNode(msg);
        }
    }

    private void addToPeerList(String ip, int port) throws IOException {
        Peer peer = new Peer(ip, port);
        if(!peers.contains(peer) && (this.port != port || !ip.equals(InetAddress.getLocalHost().getHostAddress()))) {
            peers.add(peer);
            BlockMessage blockMessage = new BlockMessage(Constants.MessageConstants.NEW_CONNECTION, "");
            broadcast(blockMessage);
        }
    }

    public TransactionPool getPool() {
        return pool;
    }

    public List<Peer> getPeers() {
        return peers;
    }

    @Override
    public void onMessage(Message message) {
        //TODO: HANDLE NEW MESSAGE
        byte[] dataBytes = message.getDataBytes();
        try {
            BlockMessage msg = (BlockMessage) P2PUtil.convertFromByteArray(dataBytes);
            if(msg.getType().equals(Constants.MessageConstants.NEW_CONNECTION)) {
                addToPeerList(message.getSender().getIp(), message.getSender().getPort());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCommStatus(boolean b, String s) {
        //TODO: HANDLE COMM ERRORS
    }
}
