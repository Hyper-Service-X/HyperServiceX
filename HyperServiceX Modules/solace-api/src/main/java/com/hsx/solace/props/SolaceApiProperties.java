package com.hsx.solace.props;

import java.util.ArrayList;
import java.util.List;

/*@Component
@ConfigurationProperties("solace.api")
@PropertySource("classpath:solace-api.properties")
@Getter
@Setter*/
public class SolaceApiProperties {

    private List<String> topics = new ArrayList<>();

    private List<Queue> queues = new ArrayList<>();

    private List<TopicEndPoint> topicEndPoints = new ArrayList<>();

    private List<Subscription> subscriptions = new ArrayList<>();

}
