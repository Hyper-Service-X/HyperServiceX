/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.hsx.solace.autoconfigure.props;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//@ConfigurationProperties("solace.java")
@Getter
@Setter
public class SolaceJavaProperties {

	/**
     * Solace Message Router Host address. Port is optional and intelligently defaulted by the Solace Java API.
     */
    private String host = "localhost";

    /**
     * Solace Message Router Message-VPN.
     */
    private String msgVpn = "default";

    /**
     * Solace Message Router Client Username.
     * This field is always used in non cloud deployments.
     * Under a cloud deployment this field is used when the cloud expected value was not provided (null).
     */
    private String clientUsername = "spring-default-client-username";

    /**
     * Solace Message Router Client Password.
     * This field is always used in non cloud deployments.
     * Under a cloud deployment this field is used when the cloud expected value was not provided (null).
     */
    private String clientPassword;

    /**
     * The client name to use when connecting to Solace Message Router. This must be unique. If absent, the API will generate a reasonable client name.
     */
    private String clientName;

    /**
     * The number of times to attempt and retry a connection to the host Solace Message Router (or list of routers) during initial connection setup.
     *
     * This property is optional and defaults to the suggested value when using HA redundant router pairs as documented in the Solace PubSub+ API Developer Guide.
     */
    private int connectRetries = 1;

    /**
     * The number of times to attempt to reconnect to the host Solace Message Router (or list of routers) after an initial connected session goes down.
     *
     * This property is optional and defaults to the suggested value when using HA redundant router pairs as documented in the Solace PubSub+ API Developer Guide.
     */
    private int reconnectRetries = 5;

    /**
     * When using a host list for the HOST property, this property defines how many times to try
     * to connect or reconnect to a single host before moving to the next host in the list.
     * NOTE: This property works in conjunction with the connect and reconnect retries settings;
     * it does not replace them.
     *
     * This property is optional and defaults to the suggested value when using HA redundant router pairs as documented in the Solace PubSub+ API Developer Guide.
     */
    private int connectRetriesPerHost = 20;

    /**
     * How much time in (MS) to wait between each attempt to connect or reconnect to a host.
     * If a connect or reconnect attempt to host is not successful, the API waits for the
     * amount of time set for reconnectRetryWaitInMillis, and then makes another connect or
     * reconnect attempt.
     *
     * This property is optional and defaults to the suggested value when using HA redundant router pairs as documented in the Solace PubSub+ API Developer Guide.
     */
    private int reconnectRetryWaitInMillis = 3000;


    /**
     * API properties can be set by the attribute naming convention used in
     * fromProperties() and toProperties()
     * 
     * Example: solace.java.apiProperties.reapply_subscriptions=true
     */
	@NestedConfigurationProperty
	private final Map<String,String> apiProperties = new ConcurrentHashMap<>();


}