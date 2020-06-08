package com.hsx.common.processor.messaging;

import com.bcs.xborder.common.util.messaging.MessagingUtilService;
import com.hsx.solace.autoconfigure.props.SolaceDefaultProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name = "hsx.messaging.service", havingValue = "solace", matchIfMissing = true)
public class SolaceMessagingUtilService implements MessagingUtilService {

    @Autowired
    SolaceDefaultProperties solaceDefaultProperties;

    @Override
    public int getCurrentSiteNo() {
        return solaceDefaultProperties.getDefaultSiteNo();
    }

    @Override
    public int getTotalSiteCount() {
        return solaceDefaultProperties.getNodes().size();
    }
}
