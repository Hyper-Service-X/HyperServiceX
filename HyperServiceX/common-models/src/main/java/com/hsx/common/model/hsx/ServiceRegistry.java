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
    private String nodeType;
    private int siteNo;
    private int nodeNo;
    private boolean status;
}
