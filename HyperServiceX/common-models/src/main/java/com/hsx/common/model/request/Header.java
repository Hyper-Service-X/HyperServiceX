package com.hsx.common.model.request;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.hsx.common.model.constants.MessageType;
import com.hsx.common.model.constants.SubMessageType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;


@Getter
@Setter
@ToString
public class Header implements Serializable {

    /*Unique ID for each message*/
    private String transactionId;

    /*Unique Type for each Operation in HSX*/
    private MessageType messageType;

    /*Unique Sub Type for each MessageType in HSX*/
    private SubMessageType subMessageType;

    /*Msg Received Time*/
    private String msgCreatedTime;

    /*Msg Received Time*/
    private String msgReceivedTime;

    /* The SA node number which receive the request */
    private String msgEntryPoint;

    @JsonPOJOBuilder(buildMethodName = "build", withPrefix = "set")
    public static class Builder {
        private String transactionId;
        private String msgCreatedTime;
        private String msgReceivedTime;
        private String msgEntryPoint;
        private MessageType messageType;
        private SubMessageType subMessageType;

        public Builder() {
        }

        public Builder setTransactionId(String transactionId) {
            this.transactionId = transactionId;
            return this;
        }

        public Builder setMsgCreatedTime(String msgCreatedTime) {
            this.msgCreatedTime = msgCreatedTime;
            return this;
        }

        public Builder setMsgReceivedTime(String msgReceivedTime) {
            this.msgReceivedTime = msgReceivedTime;
            return this;
        }

        public Builder setMsgEntryPoint(String msgEntryPoint) {
            this.msgEntryPoint = msgEntryPoint;
            return this;
        }

        public Builder setMessageType(MessageType messageType) {
            this.messageType = messageType;
            return this;
        }

        public Builder setSubMessageType(SubMessageType subMessageType) {
            this.subMessageType = subMessageType;
            return this;
        }

        public Header build() {
            return new Header(this);
        }

    }

    private Header(Header.Builder builder) {
        this.transactionId = builder.transactionId;
        this.msgCreatedTime = builder.msgCreatedTime;
        this.msgReceivedTime = builder.msgReceivedTime;
        this.msgEntryPoint = builder.msgEntryPoint;
        this.messageType = builder.messageType;
        this.subMessageType = builder.subMessageType;
    }
}
