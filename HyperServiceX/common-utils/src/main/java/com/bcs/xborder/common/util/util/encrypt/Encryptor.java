package com.bcs.xborder.common.util.util.encrypt;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.salt.SaltGenerator;
import org.jasypt.salt.ZeroSaltGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service("encryptor")
public class Encryptor {
    StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();

    @Value("${ENCRYPTION.KEY}")
    String key;

    @PostConstruct
    public void init(){
        encryptor.setAlgorithm("PBEWithMD5AndTripleDES");

        encryptor.setPassword(key);

        SaltGenerator saltGenerator = new ZeroSaltGenerator();
        encryptor.setSaltGenerator(saltGenerator);
    }
    public StandardPBEStringEncryptor getTextEncryptor() {
        return encryptor;
    }
}
