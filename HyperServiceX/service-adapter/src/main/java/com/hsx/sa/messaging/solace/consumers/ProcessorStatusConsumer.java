package com.hsx.sa.messaging.solace.consumers;

import com.hsx.sa.messaging.solace.MADefaultConsumer;
import com.hsx.solace.annotations.*;

@SolaceGuaranteedMessageConsumer(
        name = "Q.HSX0${NODE.SITE.NO}.${solace.vpn-id}.SA.NFY.JSON.PROCESSOR_STATUS",
        topics = {
                "HSX0${NODE.SITE.NO}/${solace.vpn-id}/G/ADM/PROSTAUS/*/NFY/PRO*/*/V1/JSON",
                "HSX0${NODE.SITE.NO}/${solace.vpn-id}/G/ADM/PROSTAUS/*/NFY/PRO*/*/V1/JSON/>"
        },
        sites = {SolaceSite.DEFAULT})
@EndPointConfigurations(
        permission = Permission.CONSUME,
        accessType = AccessType.EXCLUSIVE)
@ConsumerFlowConfigurations(
        ackMode = AckMode.ACK_CLIENT)
public class ProcessorStatusConsumer extends MADefaultConsumer {

}
