package com.hsx.common.model.log;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RawLogMessage {

    private String transactionId;
    private String createdTimeStamp;
    private String sentOrReceivedTimeStamp;
    private String rawMsg;
}
