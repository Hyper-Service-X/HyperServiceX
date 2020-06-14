package com.hsx.common.util.messaging;

import java.util.ArrayList;
import java.util.List;

public class MessagingUtil {

    public static List<MessageSite> filterMessagingSites(List<?> allNodes, MessageSite.Collections destinations, int currentSite) {
        List<MessageSite> sites = new ArrayList<>();

        switch (destinations) {
            case ANY_EXCEPT_CURRENT:
            case ALL_EXCEPT_CURRENT:
                for (int i = 1; i <= allNodes.size(); i++) {
                    MessageSite site = MessageSite.getSite(i);
                    sites.add(site);
                }
                break;
            case ALL:
            case ANY_ONE_SITE:
                sites.add(MessageSite.getSite(currentSite));
                for (int i = 1; i <= allNodes.size(); i++) {
                    if (i != currentSite) {
                        sites.add(MessageSite.getSite(i));
                    }
                }
                break;
            default:
                sites.add(MessageSite.getSite(currentSite));

        }

        return sites;
    }
}
