/*
 Author Name: Shaik Mahaaboob Basha
 File Name: BOOperationsConsumer.java
 Description:
 Date: 28/04/2020
 */
package com.hsx.bo.messaging.solace.consumers;

import com.hsx.bo.messaging.solace.BODefaultConsumer;
import com.hsx.solace.annotations.*;


@SolaceGuaranteedMessageConsumer(
        name = "Q.XGBW0${NODE.SITE.NO}.${solace.vpn-id}.BAO.ANY.JSON.BOOPERATIONS",
        topics = {
                "XGBW0${NODE.SITE.NO}/${solace.vpn-id}/G/OPN/OPRARCOUT/*/RES/BAO*/*/*/*/*/*/*/*/*/*/*/*/*/*/V1/JSON",
                "XGBW0${NODE.SITE.NO}/${solace.vpn-id}/G/OPN/OPRARCOUT/*/RES/BAO*/*/*/*/*/*/*/*/*/*/*/*/*/*/V1/JSON/>"
        },
        sites = {SolaceSite.DEFAULT})
@EndPointConfigurations(
        permission = Permission.CONSUME,
        accessType = AccessType.EXCLUSIVE)
@ConsumerFlowConfigurations(
        ackMode = AckMode.ACK_CLIENT)
public class BOOperationsConsumer extends BODefaultConsumer {

}
