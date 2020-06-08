package com.hsx.common.processor.service;

import com.bcs.xborder.common.util.util.JSONUtil;
import com.hsx.common.model.constants.Constants;
import com.hsx.common.model.constants.MessageType;
import com.hsx.common.model.request.BORequest;
import com.hsx.common.model.request.HSXOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component("JsonObjectResolver")
public class JsonObjectResolver implements ObjectResolver<Message<String>, HSXOperation> {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonObjectResolver.class);

    @Override
    public String getName() {
        return JsonObjectResolver.class.getName();
    }

    public HSXOperation resolve(Message<String> msg) throws IOException {
        HSXOperation result = null;
        String payload = msg.getPayload();
        MessageType MessageType = msg.getHeaders().get(Constants.MessageHeaders.MESSAGE_TYPE.name(), com.hsx.common.model.constants.MessageType.class);
        switch (MessageType) {
            default:
                JSONUtil.convertToObject(payload, BORequest.class);
//                result = JSONUtil.convertToObject(payload, new TypeReference<BORequest<SignOnOffRequest>>() {
//                });
        }
        return result;
    }

    @Override
    public String convert(Object payload) throws IOException {
        return JSONUtil.convertToJSONString(payload);
    }
}
