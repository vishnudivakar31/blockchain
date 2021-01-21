package io.vdev.dapp;

import io.vdev.socket.Message;
import io.vdev.socket.Node;
import io.vdev.socket.NodeListener;
import io.vdev.socket.Sender;
import io.vdev.util.Constants;
import io.vdev.util.P2PUtil;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
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
    private final String currentIPPort;
    private final String knownIpPort;

    private BlockNode(int port) throws NoSuchAlgorithmException, IOException {
        this.node = new Node(port, this);
        this.pool = new TransactionPool();
        this.wallet = Wallet.getInstance();
        this.peers = new ArrayList<>();
        this.port = port;
        this.currentIPPort = InetAddress.getLocalHost().getHostAddress() + ":" + port;
        this.knownIpPort = Constants.KNOWN_HOST_IP + ":" + Constants.KNOWN_HOST_PORT;
        System.out.println("node started on " + port + " epoch: " + System.currentTimeMillis());
        this.peers.add(new Peer(InetAddress.getLocalHost().getHostAddress(), port));
        if(!this.currentIPPort.equals(this.knownIpPort)) {
            this.peerDiscovery();
        }
    }

    private void peerDiscovery() throws IOException {
        System.out.println("discovering nodes...");
        Peer peer = new Peer(InetAddress.getLocalHost().getHostAddress(), this.port);
        BlockMessage blockMessage = new BlockMessage(peer, Constants.MessageConstants.NEW_CONNECTION, this.peers);
        Message msg = new Message(new Sender(Constants.KNOWN_HOST_IP, Constants.KNOWN_HOST_PORT),
                P2PUtil.convertToByteArray(blockMessage));
        this.node.sendToNode(msg);
    }

    public static BlockNode getInstance(int port) throws NoSuchAlgorithmException, IOException {
        if(instance == null) {
            instance = new BlockNode(port);
        }
        return instance;
    }

    private void broadcast(BlockMessage message) throws IOException {
        for(Peer peer : peers) {
            message.setPeer(new Peer(InetAddress.getLocalHost().getHostAddress(), this.port));
            Sender sender = new Sender(peer.getIp(), peer.getPort());
            Message msg = new Message(sender, P2PUtil.convertToByteArray(message));
            this.node.sendToNode(msg);
        }
    }

    private void addToPeerList(List<Peer> peers) throws IOException {
        boolean changed = false;
        for(Peer peer : peers) {
            if(!this.peers.contains(peer)) {
                changed = true;
                this.peers.add(peer);
            }
        }
        if(changed) {
            BlockMessage blockMessage = new BlockMessage(Constants.MessageConstants.NEW_CONNECTION, this.peers);
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
                addToPeerList((List<Peer>)msg.getData());
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
