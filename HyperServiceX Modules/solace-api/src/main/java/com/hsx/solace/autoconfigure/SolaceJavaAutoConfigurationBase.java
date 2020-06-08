package com.hsx.solace.autoconfigure;

import com.hsx.solace.autoconfigure.props.SolaceDefaultProperties;
import com.hsx.solace.autoconfigure.props.SolaceJavaProperties;
import com.hsx.solace.jcsmp.SpringJCSMPFactory;
import com.hsx.solace.jcsmp.SpringJCSMPFactoryCloudFactory;
import com.solace.services.core.model.SolaceServiceCredentials;
import com.solace.services.core.model.SolaceServiceCredentialsImpl;
import com.solace.spring.cloud.core.SolaceMessagingInfo;
import com.solacesystems.jcsmp.JCSMPChannelProperties;
import com.solacesystems.jcsmp.JCSMPProperties;
import org.springframework.context.annotation.Bean;

import java.util.*;


//Todo : Should be updated to handle list of properties ( Refer to SolaceJavaAutoConfigurationBase changes comparing to original library
//Todo : @github.com/SolaceProducts/solace-java-spring-boot
abstract class SolaceJavaAutoConfigurationBase implements SpringJCSMPFactoryCloudFactory {

    private SolaceDefaultProperties nodeProps;

    SolaceJavaAutoConfigurationBase(SolaceDefaultProperties nodeProps) {
        this.nodeProps = nodeProps;
    }

    abstract SolaceServiceCredentials findFirstSolaceServiceCredentialsImpl();

    @Override
    public abstract List<SolaceServiceCredentials> getSolaceServiceCredentials();

    @Bean
    @Override
    public SolaceServiceCredentials findFirstSolaceServiceCredentials() {
        return findFirstSolaceServiceCredentialsImpl();
    }

    @Bean
    @Override
    public SpringJCSMPFactory getSpringJCSMPFactory() {
        return getSpringJCSMPFactory(findFirstSolaceServiceCredentialsImpl());
    }

    @Override
    public SpringJCSMPFactory getSpringJCSMPFactory(String id) {
        SolaceServiceCredentials solaceServiceCredentials = findSolaceServiceCredentialsById(id);
        return solaceServiceCredentials == null ? null : getSpringJCSMPFactory(solaceServiceCredentials);
    }

    @Override
    public SpringJCSMPFactory getSpringJCSMPFactory(SolaceServiceCredentials solaceServiceCredentials) {
        return new SpringJCSMPFactory(getJCSMPProperties(solaceServiceCredentials), nodeProps.getDefaultSiteNo());
    }

    @Bean
    @Override
    public List<JCSMPProperties> getJCSMPProperties() {
        return getJCSMPProperties(findFirstSolaceServiceCredentialsImpl());
    }

    //Todo ; Later Modify this method if implementing cloud foundry implmentation
    /*@Override
    public JCSMPProperties getJCSMPProperties(String id) {
        SolaceServiceCredentials solaceServiceCredentials = findSolaceServiceCredentialsById(id);
        return solaceServiceCredentials == null ? null : getJCSMPProperties(solaceServiceCredentials);
    }*/

    @Override
    public JCSMPProperties getJCSMPProperties(String id) {
        SolaceServiceCredentials solaceServiceCredentials = findSolaceServiceCredentialsById(id);
        return solaceServiceCredentials == null ? null : getJCSMPProperties(solaceServiceCredentials).get(0);
    }

    @Override
    public List<JCSMPProperties> getJCSMPProperties(SolaceServiceCredentials solaceServiceCredentials) {
        List<JCSMPProperties> propertiesList = new ArrayList<>();

        for (SolaceJavaProperties properties : nodeProps.getNodes()) {
            Properties p = new Properties();
            Set<Map.Entry<String, String>> set = properties.getApiProperties().entrySet();
            for (Map.Entry<String, String> entry : set) {
                p.put("jcsmp." + entry.getKey(), entry.getValue());
            }

            JCSMPProperties jcsmpProps = createFromApiProperties(p);
            SolaceServiceCredentials creds = solaceServiceCredentials != null ?
                    solaceServiceCredentials : new SolaceServiceCredentialsImpl();

            jcsmpProps.setProperty(JCSMPProperties.HOST, creds.getSmfHost() != null ?
                    creds.getSmfHost() : properties.getHost());

            jcsmpProps.setProperty(JCSMPProperties.VPN_NAME, creds.getMsgVpnName() != null ?
                    creds.getMsgVpnName() : properties.getMsgVpn());

            jcsmpProps.setProperty(JCSMPProperties.USERNAME, creds.getClientUsername() != null ?
                    creds.getClientUsername() : properties.getClientUsername());

            jcsmpProps.setProperty(JCSMPProperties.PASSWORD, creds.getClientPassword() != null ?
                    creds.getClientPassword() : properties.getClientPassword());

            if ((properties.getClientName() != null) && (!properties.getClientName().isEmpty())) {
                jcsmpProps.setProperty(JCSMPProperties.CLIENT_NAME, properties.getClientName());
            }

            // Channel Properties
            JCSMPChannelProperties cp = (JCSMPChannelProperties) jcsmpProps
                    .getProperty(JCSMPProperties.CLIENT_CHANNEL_PROPERTIES);
            cp.setConnectRetries(properties.getConnectRetries());
            cp.setReconnectRetries(properties.getReconnectRetries());
            cp.setConnectRetriesPerHost(properties.getConnectRetriesPerHost());
            cp.setReconnectRetryWaitInMillis(properties.getReconnectRetryWaitInMillis());

            propertiesList.add(jcsmpProps);
        }

        return propertiesList;
    }

    @Override
    @Deprecated
    public List<SolaceMessagingInfo> getSolaceMessagingInfos() {
        return null;
    }

    private JCSMPProperties createFromApiProperties(Properties apiProps) {
        return apiProps != null ? JCSMPProperties.fromProperties(apiProps) : new JCSMPProperties();
    }

    private SolaceServiceCredentials findSolaceServiceCredentialsById(String id) {
        for (SolaceServiceCredentials credentials : getSolaceServiceCredentials())
            if (credentials.getId().equals(id)) return credentials;
        return null;
    }

}
