package com.bcs.xborder.common.util.messaging;


import com.hsx.common.model.constants.MessageType;
import com.hsx.solace.autoconfigure.props.AbstractSolaceTopicsUtil;
import com.hsx.solace.autoconfigure.props.SolaceDefaultProperties;
import com.hsx.solace.autoconfigure.props.SolaceJavaProperties;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isEmpty;

@Service
public class MessageTopicsUtil extends AbstractSolaceTopicsUtil {


    private final String NOT_AVAILABLE = "NA";

    @Autowired
    private SolaceDefaultProperties solaceDefaultProperties;

    @Value("${NODE.SITE.NO}")
    private int currentSiteNo;

    public List<MessageTopic> getTopic(MessageType MessageType, MessageOperation messageOperation, MessageDirection direction, MessageSite.Collections destinations, String messageEntryPoint, String... customValues) {
        List<MessageTopic> siteTopicMap = new ArrayList<>();
        List<SolaceJavaProperties> solaceNodes = solaceDefaultProperties.getNodes();
        List<MessageSite> siteList = MessagingUtil.filterMessagingSites(solaceNodes, destinations, currentSiteNo);
        for (MessageSite site : siteList) {
            siteTopicMap.add(
                    getTopic(
                            MessageType,
                            site,
                            messageOperation,
                            direction,
                            messageEntryPoint,
                            customValues));
        }
        return siteTopicMap;
    }


    public MessageTopic getTopic(MessageType MessageType, MessageSite site, String messageEntryPoint) {
        return new MessageTopic(
                this.getTemplate("v1", MessageType.getCode()),
                MessageType,
                null,
                null,
                "v1",
                site,
                messageEntryPoint,
                null
        );
    }

    public MessageTopic getTopic(MessageType MessageType, MessageSite site, MessageOperation messageOperation, MessageDirection direction, String messageEntryPoint, String... customValues) {
        return new MessageTopic(
                this.getTemplate("v1", MessageType.getCode()),
                MessageType,
                messageOperation,
                direction,
                "v1",
                site,
                messageEntryPoint,
                customValues
        );
    }

    public String generateTopicString(MessageTopic topic) {
        String[] customValues = topic.getCustomValues();
        String version = topic.getVersion();
        MessageType MessageType = topic.getMessageType();

        Map<String, String> valuePair = new HashMap<>();
        valuePair.put("messageEntryPoint", getNonNullValue(topic.getMessageEntryPoint()));
        valuePair.put("operation", getNonNullValue(topic.getMessageOperation() != null ? topic.getMessageOperation().getCode() : null));
        //TODO ADD more levels after #LEVELS

        return StringSubstitutor.replace(this.getTemplate(version, MessageType.name()), valuePair, "$[", "]") + (customValues != null && customValues.length > 0 ? "/" + String.join("/", customValues) : "");

    }

    String getNonNullValue(String value) {
        return isEmpty(value) ? NOT_AVAILABLE : value;
    }


}
