package com.bcs.xborder.common.util.messaging;

public enum MessagingSite {
    DEFAULT(-1),
    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6),
    SEVEN(7),
    EIGHT(8),
    NINE(9),
    TEN(10);


    private int siteNo;

    private MessagingSite(int siteNo) {
        this.siteNo = siteNo;
    }

    public int getSiteNo() {
        return siteNo;
    }

    public static MessagingSite getSite(int no) {
        for (MessagingSite site : MessagingSite.values()) {
            if (site.getSiteNo() == no)
                return site;
        }
        return DEFAULT;
    }

    public enum Collections {
        NONE,
        ALL,
        ALL_EXCEPT_CURRENT,
        ANY_EXCEPT_CURRENT,
        ANY_ONE_SITE;
    }
}
