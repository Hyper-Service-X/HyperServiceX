package com.hsx.processor.messaging.consumers;

import com.hsx.processor.messaging.ProcessorDefaultConsumer;
import com.hsx.solace.annotations.*;

@SolaceGuaranteedMessageConsumer(
        name = "Q.HSX0${NODE.SITE.NO}.${solace.vpn-id}.PRO.NFY.JSON.SA_STATUS",
        topics = {
                "HSX0${NODE.SITE.NO}/${solace.vpn-id}/G/ADM/SASTATUS/*/NFY/SA*/*/V1/JSON",
                "HSX0${NODE.SITE.NO}/${solace.vpn-id}/G/ADM/SASTATUS/*/NFY/SA*/*/V1/JSON/>"
        },
        sites = {SolaceSite.DEFAULT})
@EndPointConfigurations(
        permission = Permission.CONSUME,
        accessType = AccessType.EXCLUSIVE)
@ConsumerFlowConfigurations(
        ackMode = AckMode.ACK_CLIENT)
public class SAStatusConsumer extends ProcessorDefaultConsumer {

}
