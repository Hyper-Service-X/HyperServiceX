package com.hsx.common.util.messaging;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MessageTopic {

    private String topicTemplate;

    private com.hsx.common.model.constants.MessageType MessageType;
    private MessageOperation messageOperation;
    private MessageDirection direction;
    private String version;
    private MessageSite destinationSite;
    private String messageEntryPoint;


    private String[] customValues;
}
