package com.hsx.bo.properties;

import com.hsx.common.model.constants.Constants;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.support.PropertiesLoaderSupport;

import java.io.IOException;
import java.util.Base64;
import java.util.Enumeration;
import java.util.Properties;

public class EncryptedPropertiesFactoryBean extends PropertiesLoaderSupport implements FactoryBean<Properties>, InitializingBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(EncryptedPropertiesFactoryBean.class);

    private boolean singleton = true;
    private Properties singletonInstance;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (this.singleton) {
            this.singletonInstance = createProperties();
        }
    }

    @Override
    public Properties getObject() throws Exception {
        if (this.singleton) {
            return this.singletonInstance;
        } else {
            return createProperties();
        }
    }

    @Override
    public Class<?> getObjectType() {
        return Properties.class;
    }

    @Override
    public boolean isSingleton() {
        return this.singleton;
    }

    private Properties createProperties() throws IOException {
        Properties properties = mergeProperties();

        String passwordEncryptionAlgorithm = properties.getProperty("PASSWORD.ENCRYPTION.ALGORITHM");
        String encryptionPasscode = properties.getProperty("PASSWORD.ENCRYPTION.PASSCODE");

        Properties propertiesNew = new Properties();
        String key = null;
        Enumeration enumeration = properties.propertyNames();
        while (enumeration.hasMoreElements()) {
            key = (String) enumeration.nextElement();
            String value = (String) properties.get(key);
            if (value.startsWith(Constants.Encryption.ENCRYPTION_PREFIX) &&
                    value.endsWith(Constants.Encryption.ENCRYPTION_POSTFIX)) {
                String encryptedValue = value;
                String encryptedStripped = encryptedValue.substring(4, encryptedValue.length() - 1);
                String decryptedProperty = getStandardPBEStringEncryptor(passwordEncryptionAlgorithm, encryptionPasscode).decrypt(encryptedStripped);
                propertiesNew.put(key, decryptedProperty);
            } else {
                propertiesNew.put(key, properties.get(key));
            }
        }
        return propertiesNew;
    }

    private StandardPBEStringEncryptor getStandardPBEStringEncryptor(String passwordEncryptionAlgorithm, String encryptionPasscode) {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setAlgorithm(passwordEncryptionAlgorithm);
        encryptor.setPassword(new String(Base64.getDecoder().decode(encryptionPasscode)));

        return encryptor;
    }
}
