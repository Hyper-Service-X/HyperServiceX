package com.hsx.common.model.hsx;

import lombok.*;

/**
 * @author Nadith
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ServiceRegistry {
    private String serviceName;
    private String nodeName;
    private boolean status;
    private boolean master;

    private String lastUpdatedTime;
    private String statusFrom;
    private String lastAvailableTime;

    public ServiceRegistry(String serviceName, String nodeName, boolean status) {
        this.serviceName = serviceName;
        this.nodeName = nodeName;
        this.status = status;
    }
}
