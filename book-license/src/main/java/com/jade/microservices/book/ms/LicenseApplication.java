package com.jade.microservices.book.ms;

import com.jade.microservices.book.ms.config.service.ServiceConfig;
import com.jade.microservices.book.ms.util.UserContextInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.metrics.Metric;
import org.springframework.boot.actuate.metrics.writer.Delta;
import org.springframework.boot.actuate.metrics.writer.MetricWriter;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.sleuth.Sampler;
import org.springframework.cloud.sleuth.sampler.AlwaysSampler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * @Author: Josiah Adetayo
 * @Email: j.adetayo@bcs.org.uk, josiah.adetayo@cwg-plc.com
 * @Date: 5/6/18
 */
@Slf4j
@SpringBootApplication
@EnableCircuitBreaker // Enables the service to use Hystrix and Ribbon libraries - Client Resilient Pattern
@EnableDiscoveryClient // This activates the Spring Discovery Client - is the trigger for Spring Cloud to enable
//the application to use the DiscoveryClient and Ribbon libraries
@EnableResourceServer
@EnableFeignClients // Will be used much later
//@EnableBinding(Sink.class)
public class LicenseApplication {

    @Autowired
    ServiceConfig serviceConfig;

    public static void main(String[] args) {
        SpringApplication.run(LicenseApplication.class, args);
    }

    /*@StreamListener(Sink.INPUT)
    public void logConsumer(OrganizationChangeModel orgChange) {
        log.debug("Received an event for organization Id: {}\n {}", orgChange.getOrgId(), orgChange);
    }*/

    @Bean
    public Sampler sampler() {
        return new AlwaysSampler();
    }

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {

        JedisConnectionFactory redisConnectionFactory = new JedisConnectionFactory();

        redisConnectionFactory.setHostName(serviceConfig.getRedisHost());
        redisConnectionFactory.setPort(serviceConfig.getRedisPort());

        return redisConnectionFactory;

    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {

        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

        redisTemplate.setConnectionFactory(jedisConnectionFactory());

        return redisTemplate;

    }


    // Note: The Ribbon-backed RestTemplate does not need the @EnableDiscoveryClient and @EnableFeignClients to work
    @LoadBalanced
    @Primary
    @Bean
    public RestTemplate restTemplate() {
        //return new RestTemplate();

        //Adding the UserContextInterceptor to the RestTemplate instance that has been created
        RestTemplate template = new RestTemplate();
        List<ClientHttpRequestInterceptor> templates = template.getInterceptors();

        if(templates == null) {
            template.setInterceptors(Collections.singletonList(new UserContextInterceptor()));
        }else {
            templates.add(new UserContextInterceptor());
            template.setInterceptors(templates);
        }

        return template;

    }


    @Bean
    public OAuth2RestTemplate oauth2RestTemplate(@Qualifier("oauth2ClientContext") OAuth2ClientContext oauth2ClientContext,
                                                 OAuth2ProtectedResourceDetails details) {
        return new OAuth2RestTemplate(details, oauth2ClientContext);
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        Resource resource = new ClassPathResource("publickey.txt");
        String publicKey = null;
        try {
            publicKey = IOUtils.toString(resource.getInputStream());
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
        converter.setVerifierKey(publicKey);
        return converter;
    }


    @Bean
    public MetricWriter getMetricWriter() {
        return new MetricWriter() {
            @Override
            public void increment(Delta<?> delta) {

            }

            @Override
            public void reset(String s) {

            }

            @Override
            public void set(Metric<?> metric) {

            }
        };
    }
}
