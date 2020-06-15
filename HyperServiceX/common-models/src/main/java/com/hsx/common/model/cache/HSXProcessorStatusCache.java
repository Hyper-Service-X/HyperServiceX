package com.hsx.common.model.cache;

import com.hsx.common.model.hsx.ServiceRegistry;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author Nadith
 */
public enum HSXProcessorStatusCache {
    INSTANCE;
    Map<String, ServiceRegistry> processorStatusMap = new ConcurrentHashMap<>();

    public Map<String, ServiceRegistry> getCache() {
        return processorStatusMap;
    }

    public ServiceRegistry getProcessorStatus(String nodeName) {
        if (processorStatusMap.containsKey(nodeName)) {
            return processorStatusMap.get(nodeName);
        }
        return null;
    }

    public List<ServiceRegistry> findAllByStatus(String serviceName, boolean status) {
        if (processorStatusMap.values() != null && !processorStatusMap.values().isEmpty()) {
            return processorStatusMap.values().stream().filter(a -> serviceName.equals(a.getServiceName()) && status == a.isStatus()).collect(Collectors.toList());
        }
        return null;
    }

}
