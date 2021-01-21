package io.vdev.dapp;

import java.io.Serializable;

public class BlockMessage implements Serializable {
    private Peer peer;
    private String type;
    private Object data;

    public BlockMessage(String type, Object data) {
        this.type = type;
        this.data = data;
    }

    public BlockMessage(Peer peer, String type, Object data) {
        this.peer = peer;
        this.type = type;
        this.data = data;
    }

    public void setPeer(Peer peer) {
        this.peer = peer;
    }

    public Peer getPeer() {
        return peer;
    }

    public String getType() {
        return type;
    }

    public Object getData() {
        return data;
    }
}
