package com.bcs.xborder.common.util.util;

import org.apache.commons.text.StringSubstitutor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CommonUtil {

    public static String PRXY_ONLY_RTVL_DELIMITER = "\\|_\\|";
    public static String XB_PROXY_ONLY_RTVL_VALUE_TEMPLATE = "${PROXY_RTVL_TYPE}|_|${PROXY_RTVL_VALUE}|_|${PROXY_RTVL_CC_CODE}|_|${PROXY_RTVL_TRANSACTION_AMT}";

    public static String PROXY_RTVL_TYPE = "PROXY_RTVL_TYPE";
    public static String PROXY_RTVL_VALUE = "PROXY_RTVL_VALUE";
    public static String PROXY_RTVL_CC_CODE = "PROXY_RTVL_CC_CODE";
    public static String PROXY_RTVL_TRANSACTION_AMT = "PROXY_RTVL_TRANSACTION_AMT";

    public static String generateProxyOnlyRtvlValue(String proxyType, String proxyValue, String currencyCode, String transactionAmt) {
        Map<String, String> values = new HashMap<>();
        values.put(PROXY_RTVL_TYPE, proxyType);
        values.put(PROXY_RTVL_VALUE, proxyValue);
        values.put(PROXY_RTVL_CC_CODE, currencyCode);
        values.put(PROXY_RTVL_TRANSACTION_AMT, transactionAmt);

        return StringSubstitutor.replace(XB_PROXY_ONLY_RTVL_VALUE_TEMPLATE, values);
    }


    public static Map<String, String> generateProxyOnlyRtvl(String rtvlStr) {
        Map<String, String> values = new HashMap<>();
        String[] rtvlValues = rtvlStr.split(PRXY_ONLY_RTVL_DELIMITER);
        values.put(PROXY_RTVL_TYPE, rtvlValues[0]);
        values.put(PROXY_RTVL_VALUE, rtvlValues[1]);
        values.put(PROXY_RTVL_CC_CODE, rtvlValues[2]);
        values.put(PROXY_RTVL_TRANSACTION_AMT, rtvlValues[3]);
        return values;
    }

    public static String maskString(String strText, int start, int end, char maskChar) {

        if (strText == null || strText.equals("") || strText.length() <= 4)
            return strText;

        if (start < 0)
            start = 0;

        if (end > strText.length())
            end = strText.length();


        int maskLength = end - start;

        if (maskLength == 0)
            return strText;

        StringBuilder sbMaskString = new StringBuilder(maskLength);

        for (int i = 0; i < maskLength; i++) {
            sbMaskString.append(maskChar);
        }

        return strText.substring(0, start)
                + sbMaskString.toString()
                + strText.substring(start + maskLength);
    }

}
