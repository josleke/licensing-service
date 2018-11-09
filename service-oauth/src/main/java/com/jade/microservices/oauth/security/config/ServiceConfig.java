package com.jade.microservices.oauth.security.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.stereotype.Component;

import java.security.KeyPair;

/**
 * @Author: Josiah Adetayo
 * @Email: j.adetayo@bcs.org.uk, josiah.adetayo@cwg-plc.com
 * @Date: 9/14/18
 */
@Data
@Component
@Configuration
public class ServiceConfig {

    @Value("${jwt.signing.key}")
    private String signingKey = "";

    @Value("${jwt.store.name}")
    private String storeName;

    private KeyPair keyPair;
//    private KeyStoreKeyFactory keyStoreKeyFactory;

    /*public ServiceConfig() {

        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(
                                    new ClassPathResource(storeName+".jks"), "josleke.123".toCharArray());

        keyPair = keyStoreKeyFactory.getKeyPair(storeName);
    }*/

    public KeyPair getKeyPair() {

        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(
                new ClassPathResource(storeName+".jks"), "josleke.123".toCharArray());

        return keyStoreKeyFactory.getKeyPair(storeName);


    }
}
