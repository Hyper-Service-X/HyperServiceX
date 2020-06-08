package com.hsx.solace;

import com.hsx.solace.autoconfigure.props.SolaceDefaultProperties;
import com.hsx.solace.jcsmp.SpringJCSMPFactory;
import com.hsx.solace.annotations.*;
import com.solacesystems.jcsmp.*;
import com.solacesystems.jcsmp.Queue;
import org.apache.commons.text.StringSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.env.*;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.StreamSupport;

@Component
@ConditionalOnProperty(name = "hsx.messaging.service", havingValue = "solace", matchIfMissing = true)
public class SolaceInitializer {

    private SolaceSessionService sessionService;

    private SpringJCSMPFactory solaceFactory;

    private final HSXJCSMPSession defaultSession;
    private XMLMessageConsumer cons;
    private List<FlowReceiver> flows;
    private final SolaceDefaultProperties solaceDefaultProperties;

    private Environment env;

    Map<String, Object> properties = new HashMap<>();
    private AutowireCapableBeanFactory autowireCapableBeanFactory;

    private final Logger LOGGER = LoggerFactory.getLogger(SolaceInitializer.class);
    private ApplicationContext ctx;


    public SolaceInitializer(ApplicationContext ctx) throws JCSMPException {
        //todo : add mechanism to keep trying if connection fails
        this.solaceFactory = ctx.getBean(SpringJCSMPFactory.class);
        this.sessionService = ctx.getBean(SolaceSessionService.class);
        this.solaceDefaultProperties = ctx.getBean(SolaceDefaultProperties.class);
        this.env = ctx.getEnvironment();
        sessionService.connectToAllSessions();
        autowireCapableBeanFactory = ctx.getAutowireCapableBeanFactory();
        this.ctx = ctx;
        this.defaultSession = sessionService.getDefaultSession();

        MutablePropertySources propSrcs = ((AbstractEnvironment) env).getPropertySources();
        StreamSupport.stream(propSrcs.spliterator(), false)
                .filter(ps -> ps instanceof EnumerablePropertySource)
                .map(ps -> ((EnumerablePropertySource) ps).getPropertyNames())
                .flatMap(Arrays::<String>stream)
                .forEach(propName -> properties.put(propName, env.getProperty(propName)));

        this.flows = new ArrayList<>();
    }


    @PostConstruct
    public void initConsumers() {

        initializeDirectMessageConsumers();

        initializeGuaranteedMessageConsumers();

    }


    @PreDestroy
    public void clearConnection() {
        if (cons != null)
            cons.close();

        if (flows != null && !flows.isEmpty()) {
            for (FlowReceiver flow : flows) {
                flow.close();
            }
        }

        sessionService.closeAllSessions();
    }

    private void initializeGuaranteedMessageConsumers() {

        //Scan for guaranteed message consumer and configure
        ClassPathScanningCandidateComponentProvider solaceQueueConsumersFilter =
                new ClassPathScanningCandidateComponentProvider(false);
        solaceQueueConsumersFilter.addIncludeFilter(new AnnotationTypeFilter(SolaceGuaranteedMessageConsumer.class));
        Set<BeanDefinition> queueConsumerBeans = solaceQueueConsumersFilter.findCandidateComponents("*");

        if (queueConsumerBeans != null) {
            for (BeanDefinition bd : queueConsumerBeans) {
                try {
                    final Class<?> tc = Class.forName(bd.getBeanClassName());
                    SolaceGuaranteedMessageConsumer annotation = tc.getAnnotation(SolaceGuaranteedMessageConsumer.class);
                    EndPointConfigurations endPointConfigurations = tc.getAnnotation(EndPointConfigurations.class);
                    ConsumerFlowConfigurations flowConfigurations = tc.getAnnotation(ConsumerFlowConfigurations.class);
                    String[] topics = annotation.topics();
                    String name = annotation.name();

                    List<SolaceSite> siteList = Arrays.asList(annotation.sites());
                    SolaceSite.Collections siteCollection = annotation.siteCollection();

                    Constructor<?> constructor = tc.getConstructor();
                    EndpointProperties endpointProps = generateEndPointProperties(endPointConfigurations);
                    ConsumerFlowProperties flowProperties = generateConsumerFlowProperties(flowConfigurations);


                    List<HSXJCSMPSession> filteredSessions = new ArrayList<>();

                    if (siteCollection == SolaceSite.Collections.ALL || siteCollection == SolaceSite.Collections.ANY_ONE_SITE) {
                        filteredSessions = sessionService.getAllSessions();
                    } else if (siteCollection == SolaceSite.Collections.ALL_EXCEPT_CURRENT || siteCollection == SolaceSite.Collections.ANY_EXCEPT_CURRENT) {
                        filteredSessions = sessionService.getAllExceptDefaultSessions();
                    } else {
                        for (SolaceSite site : siteList) {
                            switch (site) {
                                case DEFAULT:
                                    filteredSessions.add(sessionService.getDefaultSession());
                                    break;
                                default:
                                    filteredSessions.add(sessionService.getAllSessions().get(site.getSiteNo() - 1));
                                    break;
                            }
                        }
                    }

                    String threadCount = subEnvVariables(annotation.threadCount()).trim();

                    for (HSXJCSMPSession session : filteredSessions) {
                        startFlowReceiver(session, name, endpointProps, flowProperties, topics, constructor, Integer.parseInt(threadCount));
                    }

                } catch (Exception e) {
                    LOGGER.error("Error occurred in initializing queue for bean {} ", bd.getBeanClassName());
                    e.printStackTrace();
                    System.exit(1);
                }
            }
        }
    }


    private void startFlowReceiver(HSXJCSMPSession HSXJCSMPSession, String queueName, EndpointProperties endpointProps, ConsumerFlowProperties flowProperties, String[] topics, Constructor<?> constructor, int threadCount) throws JCSMPException, IllegalAccessException, InvocationTargetException, InstantiationException {
        LOGGER.info("Initializing flow for site {} queue name {}", HSXJCSMPSession.getSiteNo(), queueName);
        queueName = subEnvVariables(queueName);
        JCSMPSession session = HSXJCSMPSession.getSession();
        final Queue queue = JCSMPFactory.onlyInstance().createQueue(queueName);

        session.provision(queue, endpointProps, JCSMPSession.FLAG_IGNORE_ALREADY_EXISTS);

        Set<Subscription> subscriptions = session.getSubscriptionCache();

        //Remove all existing subscriptions
        if (subscriptions != null && !subscriptions.isEmpty()) {
            for (Subscription subscription : subscriptions) {
                session.removeSubscription(subscription, true);
            }
        }

        if (topics != null &&
                session.isCapable(CapabilityType.PUB_GUARANTEED) &&
                session.isCapable(CapabilityType.SUB_FLOW_GUARANTEED) &&
                session.isCapable(CapabilityType.ENDPOINT_MANAGEMENT) &&
                session.isCapable(CapabilityType.QUEUE_SUBSCRIPTIONS)) {

            for (String topic : topics) {
                Topic tutorialTopic = JCSMPFactory.onlyInstance().createTopic(subEnvVariables(topic));
                session.addSubscription(queue, tutorialTopic, JCSMPSession.WAIT_FOR_CONFIRM);
            }
        }

        flowProperties.setEndpoint(queue);

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        executorService.submit(() -> {
            try {
                LOGGER.info("Going to  created flow for bean {} topics {}", constructor.getDeclaringClass().getName(), topics);

                Object msgConsumer = constructor.newInstance();
                autowireCapableBeanFactory.autowireBean(msgConsumer);
                autowireCapableBeanFactory.initializeBean(msgConsumer, constructor.getDeclaringClass().getSimpleName());
                FlowReceiver flowReceiver = session.createFlow((XMLMessageListener) msgConsumer, flowProperties, endpointProps);
                flowReceiver.start();
                flows.add(flowReceiver);
                LOGGER.info("Successfully created flow for bean {} topics {}", constructor.getDeclaringClass().getName(), topics);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (JCSMPException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });


        LOGGER.info("Flow successfully started for site {} queue name {} ", HSXJCSMPSession.getSiteNo(), queueName);
    }

    // Direct messages are only subscribing to default session. Need to be improved if cross site communication expected
    private void initializeDirectMessageConsumers() {
        //Scan for solace topic end point consumer annotation and subscribe to all topics related to it
        ClassPathScanningCandidateComponentProvider solaceDMConsumerScanner =
                new ClassPathScanningCandidateComponentProvider(false);
        solaceDMConsumerScanner.addIncludeFilter(new AnnotationTypeFilter(SolaceDirectMessageConsumer.class));
        Set<BeanDefinition> beanDefinitions = solaceDMConsumerScanner.findCandidateComponents("*");
        if (beanDefinitions != null && beanDefinitions.iterator().hasNext()) {
            BeanDefinition bd = beanDefinitions.iterator().next();
            try {
                final Class<?> tc = Class.forName(bd.getBeanClassName());
                String topicsStr = tc.getAnnotation(SolaceDirectMessageConsumer.class).topics();
                if (topicsStr != null) {
                    Constructor<?> constructor = tc.getConstructor();
                    Object msgConsumer = constructor.newInstance();
                    String[] topics = topicsStr.split(",");
                    cons = defaultSession.getSession().getMessageConsumer((XMLMessageListener) msgConsumer);
                    for (String topic : topics) {
                        defaultSession.getSession().addSubscription(JCSMPFactory.onlyInstance().createTopic(subEnvVariables(topic)));
                    }
                    cons.start();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private ConsumerFlowProperties generateConsumerFlowProperties(ConsumerFlowConfigurations annotation) {

        ConsumerFlowProperties consumerFlowProperties = new ConsumerFlowProperties();
        consumerFlowProperties.setSelector(Constants.NULL.equals(annotation.sqlSelector()) ? null : annotation.sqlSelector());
        consumerFlowProperties.setStartState(annotation.startState());
        consumerFlowProperties.setNoLocal(annotation.noLocal());
        consumerFlowProperties.setActiveFlowIndication(annotation.activeFlowIndication());
        switch (annotation.ackMode()) {
            case ACK_AUTO:
                consumerFlowProperties.setAckMode(JCSMPProperties.SUPPORTED_MESSAGE_ACK_AUTO);
                break;
            case ACK_CLIENT:
                consumerFlowProperties.setAckMode(JCSMPProperties.SUPPORTED_MESSAGE_ACK_CLIENT);
                break;
            default:
                consumerFlowProperties.setAckMode(null);
        }
        consumerFlowProperties.setTransportWindowSize(annotation.windowSize());
        consumerFlowProperties.setAckTimerInMsecs(annotation.ackTimeMs());
        consumerFlowProperties.setAckThreshold(annotation.ackThreshold());
        consumerFlowProperties.setSegmentFlow(annotation.segmentFlow());
        consumerFlowProperties.setWindowedAckMaxSize(annotation.windowedAckMaxSize());

        return consumerFlowProperties;
    }

    private EndpointProperties generateEndPointProperties(EndPointConfigurations annotation) {
        EndpointProperties endpointProperties = new EndpointProperties();

        switch (annotation.accessType()) {
            case EXCLUSIVE:
                endpointProperties.setAccessType(EndpointProperties.ACCESSTYPE_EXCLUSIVE);
                break;
            case NONEXCLUSIVE:
                endpointProperties.setAccessType(EndpointProperties.ACCESSTYPE_NONEXCLUSIVE);
                break;
        }

        switch (annotation.discardBehavior()) {
            case NOTIFY_SENDER_OFF:
                endpointProperties.setDiscardBehavior(EndpointProperties.DISCARD_NOTIFY_SENDER_OFF);
                break;
            case NOTIFY_SENDER_ON:
                endpointProperties.setDiscardBehavior(EndpointProperties.DISCARD_NOTIFY_SENDER_ON);
                break;
        }

        switch (annotation.permission()) {
            case CONSUME:
                endpointProperties.setPermission(EndpointProperties.PERMISSION_CONSUME);
                break;
            case NONE:
                endpointProperties.setPermission(EndpointProperties.PERMISSION_NONE);
                break;
            case DELETE:
                endpointProperties.setPermission(EndpointProperties.PERMISSION_DELETE);
                break;
            case READ_ONLY:
                endpointProperties.setPermission(EndpointProperties.PERMISSION_READ_ONLY);
                break;
            case MODIFY_TOPIC:
                endpointProperties.setPermission(EndpointProperties.PERMISSION_MODIFY_TOPIC);
                break;
        }

        if (annotation.maxMessageSize() > 0) {
            endpointProperties.setMaxMsgSize(annotation.maxMessageSize());
        }

        if (annotation.maxMsgRedelivery() > 0) {
            endpointProperties.setMaxMsgRedelivery(annotation.maxMsgRedelivery());
        }

        if (annotation.quota() > 0) {
            endpointProperties.setQuota(annotation.quota());
        }

        endpointProperties.setRespectsMsgTTL(annotation.respectsMsgTTL());


        return endpointProperties;
    }

    public String subEnvVariables(String str) {
        return StringSubstitutor.replace(str, properties);
    }
}
