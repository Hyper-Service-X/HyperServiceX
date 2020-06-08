package com.hsx.common.model.constants;

import java.text.MessageFormat;

public class Formats {

    private Formats() {
    }

    public static final String XB_SERVICE_REGISTRY_KEY = "{0}_S{1,number}_N{2,number}";

    public static final String ERROR_OCCURRED_AT___LOG_DETAIL___ERROR_DETAIL___STACKTRACE___ = "Error Occurred @ {},Log Detail: [{}],Error Detail: {},StackTrace: {}";
    public static final String ERROR_OCCURRED_AT___SCHEDULER___LOG_DETAIL___ERROR_DETAIL___STACKTRACE___ = "Error Occurred @ {},Scheduler Name: {},Log Detail [{}],Error Detail: {},StackTrace: {}";

    public static String serviceRegistryKey(String componentType, int siteNo, int nodeNo) {
        return MessageFormat.format(XB_SERVICE_REGISTRY_KEY, componentType, siteNo,nodeNo);
    }
}
