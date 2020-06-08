package com.hsx.solace.annotations;

public enum SolaceSite {
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

    private SolaceSite(int siteNo) {
        this.siteNo = siteNo;
    }

    public int getSiteNo() {
        return siteNo;
    }

    public static SolaceSite getSite(int no) {
        for (SolaceSite site : SolaceSite.values()) {
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
