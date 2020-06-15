package com.hsx.sa.spring.integration.gw;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

/**
 * Created by
 *
 * @author Nadith on 15/6/2020
 * Modified by
 * @author ... on ... : Modified Reason is ...
 * Usage :
 * Spring Integration OperationalMessageGateway gateway
 * Subcribable,Point to Point channel with Multi Threaded execution
 * on error default error channel will be called {@link com.hsx.sa.spring.integration.error.SAGlobalErrorChannelService}
 * <p>
 * We can abstract the concept of {@link MessageChannel} using {@link MessagingGateway {@link OperationalMessageGateway }
 * while we can decouple the code between {@link Component},or {@link org.springframework.stereotype.Service}
 * .check the example implementaion {@link OperationalMessageGateway }}
 */
@MessagingGateway(
        name = "operationalMessageGateway",
        defaultRequestChannel = "operationalMessageChannel")
@Component
public interface OperationalMessageGateway {

    @Gateway
    void send(Message<String> xbInputMessage);

    //if we need a request-reply channel

//    @Gateway
//    Message<Boolean> sendAndAck(Message<?> inputMessage);


    //if we need a event publish-subscribe channel

//    @Gateway
//    void publishEvent(Message<?> inputMessage);
}
