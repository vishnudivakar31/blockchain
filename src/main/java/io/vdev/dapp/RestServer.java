package io.vdev.dapp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RestServer {
    private static RestServer instance = null;
    private BlockNode blockNode;
    private Javalin server;
    private ObjectMapper objectMapper;


    private RestServer(BlockNode blockNode) {
        this.blockNode = blockNode;
        this.objectMapper = new ObjectMapper();
    }

    public static RestServer getInstance(BlockNode blockNode) {
        if(instance == null) {
            instance = new RestServer(blockNode);
        }
        return instance;
    }

    public void start(int port) {
        server = Javalin.create().start(port);
        server.get("/known_peers", ctx -> fetchKnownPeers(ctx));
        server.get("/public_key", ctx -> fetchPublicKey(ctx));
        server.get("/transaction_pool", ctx -> fetchTransactionPool(ctx));
        server.post("/transaction", ctx -> createTransaction(ctx));
    }

    private void fetchKnownPeers(Context ctx) throws JsonProcessingException {
        List<Peer> peers = blockNode.getPeers();
        ctx.result(objectMapper.writeValueAsString(peers));
    }

    private void fetchPublicKey(Context ctx) throws JsonProcessingException {
        Map<String, String> result = new HashMap<>();
        result.put("public_key", blockNode.getWallet().getPublicKey());
        ctx.result(objectMapper.writeValueAsString(result));
    }

    private void createTransaction(Context ctx)
            throws IOException, NoSuchAlgorithmException, InvalidKeyException, SignatureException, InvalidKeySpecException {
        String postBody = ctx.body();
        Map<String, Object> body = objectMapper.readValue(postBody, Map.class);
        Transaction transaction = blockNode.getWallet().createTransaction((String) body.get("receiver_public_key"),
                (String )body.get("transaction_type"), (Double) body.get("amount"));
        blockNode.addToTransactionPool(transaction);
        ctx.status(201);
        ctx.result(objectMapper.writeValueAsString(transaction));
    }

    private void fetchTransactionPool(Context ctx) throws JsonProcessingException {
        List<Transaction> pool = blockNode.getPool().getTransactions();
        ctx.result(objectMapper.writeValueAsString(pool));
    }


}
