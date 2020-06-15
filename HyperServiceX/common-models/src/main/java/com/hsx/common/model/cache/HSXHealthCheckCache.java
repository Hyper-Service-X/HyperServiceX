package com.hsx.common.model.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Nadith
 */
public enum HSXHealthCheckCache {
    INSTANCE;
    Map<String, Boolean> healthCheckMap = new ConcurrentHashMap<>();

    public Map<String, Boolean> getCache() {
        return healthCheckMap;
    }

    public Boolean getStatus(String nodeName) {
        if (healthCheckMap.containsKey(nodeName)) {
            return healthCheckMap.get(nodeName);
        }
        return null;
    }
}
