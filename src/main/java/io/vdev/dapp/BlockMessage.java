package io.vdev.dapp;

import java.io.Serializable;

public class BlockMessage implements Serializable {
    private String type;
    private Object data;

    public BlockMessage(String type, Object data) {
        this.type = type;
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public Object getData() {
        return data;
    }
}
