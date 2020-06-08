package com.hsx.common.model.constants;

public class Constants {

    public enum MessageHeaders {
        RECEIVED_TIME,
        MESSAGE_TYPE,
        MESSAGE_OPERATION,
        MA_ENTRY_POINT,
        CUSTOM_VALUES

    }
    public class Encryption {
        public final static String KEY_ALGORITHM = "encryption.algorithm";
        public final static String KEY_PASSKEY = "encryption.key";
        public final static String ENCRYPTION_PREFIX = "ENC(";
        public final static String ENCRYPTION_POSTFIX = ")";
    }

}