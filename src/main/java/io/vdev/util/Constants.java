package io.vdev.util;

public class Constants {
    public static final String KEYS_DIRECTORY = "keys";
    public static final String PRIVATE_KEY_FILE = "keys/id_rsa";
    public static final String PUBLIC_KEY_FILE = "keys/id_rsa.pub";
    public static final String KNOWN_HOST_IP = "192.168.1.152";
    public static final Integer KNOWN_HOST_PORT = 6000;

    public static class MessageConstants {
        public static final String NEW_CONNECTION = "NEW_CONNECTION";
        public static final String NEW_TRANSACTION = "NEW_TRANSACTION";
        public static final String ENTIRE_TRANSACTION_POOL = "ENTIRE_TRANSACTION_POOL";
    }

    public static class TransactionConstants {
        public static final String RECEIVER_PUBLIC_KEY = "receiver_public_key";
        public static final String TRANSACTION_TYPE = "transaction_type";
        public static final String AMOUNT = "amount";
    }
}
