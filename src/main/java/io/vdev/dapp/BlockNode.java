package io.vdev.dapp;

import io.vdev.socket.Message;
import io.vdev.socket.Node;
import io.vdev.socket.NodeListener;
import io.vdev.socket.Sender;
import io.vdev.util.BlockchainUtils;
import io.vdev.util.Constants;
import io.vdev.util.P2PUtil;

import java.io.IOException;
import java.net.InetAddress;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlockNode implements NodeListener {
    private static BlockNode instance = null;
    private final Node node;
    private final TransactionPool pool;
    private final Wallet wallet;
    private List<Peer> peers;
    private final int port;
    private final String currentIPPort;
    private final String knownIpPort;
    private Map<String, Integer> messageCounter;

    private BlockNode(int port) throws NoSuchAlgorithmException, IOException {
        this.node = new Node(port, this);
        this.pool = new TransactionPool();
        this.wallet = Wallet.getInstance();
        this.peers = new ArrayList<>();
        this.port = port;
        this.messageCounter = new HashMap<>();
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
            messageCounter.put(Constants.MessageConstants.NEW_CONNECTION, getPeers().size());
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

    public Wallet getWallet() {
        return wallet;
    }

    public String getCurrentIPPort() {
        return currentIPPort;
    }

    public String getKnownIpPort() {
        return knownIpPort;
    }

    public void addToTransactionPool(Transaction transaction)
            throws InvalidKeySpecException, NoSuchAlgorithmException, SignatureException, InvalidKeyException, IOException {
        String signature = transaction.getSignature();
        String senderPublicKey = transaction.getSenderPublicKey();
        boolean transactionExists = pool.transactionExists(transaction);
        boolean signatureValid = Wallet.verify(transaction.payload(),
                signature, BlockchainUtils.getPublicKeyFromString(senderPublicKey));
        if(!transactionExists && signatureValid) {
            pool.addTransaction(transaction);
            BlockMessage msg = new BlockMessage(Constants.MessageConstants.NEW_TRANSACTION, transaction);
            broadcast(msg);
        }
    }

    @Override
    public void onMessage(Message message) {
        //TODO: HANDLE NEW MESSAGE
        byte[] dataBytes = message.getDataBytes();
        try {
            BlockMessage msg = (BlockMessage) P2PUtil.convertFromByteArray(dataBytes);
            if(msg.getType().equals(Constants.MessageConstants.NEW_CONNECTION)) {
                addToPeerList((List<Peer>)msg.getData());
            } else if (msg.getType().equals(Constants.MessageConstants.NEW_TRANSACTION)) {
                Transaction newTransaction = (Transaction) msg.getData();
                addToTransactionPool(newTransaction);
            } else if (msg.getType().equals(Constants.MessageConstants.ENTIRE_TRANSACTION_POOL)) {
                List<Transaction> transactions = (List<Transaction>) msg.getData();
                handleTransactionPool(transactions);
            }
        } catch (IOException | ClassNotFoundException | NoSuchAlgorithmException | InvalidKeyException | SignatureException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }

    private void handleTransactionPool(List<Transaction> transactions)
            throws NoSuchAlgorithmException, IOException, InvalidKeySpecException, InvalidKeyException, SignatureException {
        for(Transaction transaction : transactions) {
            addToTransactionPool(transaction);
        }
    }

    @Override
    public void onCommStatus(boolean b, String s, Message originalMessage) {
        byte[] dataBytes = originalMessage.getDataBytes();
        try {
            BlockMessage msg = (BlockMessage) P2PUtil.convertFromByteArray(dataBytes);
            if(msg.getType().equals(Constants.MessageConstants.NEW_CONNECTION) &&
                    messageCounter.containsKey(Constants.MessageConstants.NEW_CONNECTION)) {

                messageCounter.put(Constants.MessageConstants.NEW_CONNECTION,
                        messageCounter.get(Constants.MessageConstants.NEW_CONNECTION) - 1);
                if(messageCounter.get(Constants.MessageConstants.NEW_CONNECTION) == 0) {
                    BlockMessage newMsg = new BlockMessage(Constants.MessageConstants.ENTIRE_TRANSACTION_POOL, getPool().getTransactions());
                    broadcast(newMsg);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
