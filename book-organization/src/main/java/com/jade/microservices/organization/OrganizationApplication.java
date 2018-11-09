package com.jade.microservices.organization;

import com.jade.microservices.organization.util.UserContextFilter;
import org.apache.commons.io.IOUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.metrics.Metric;
import org.springframework.boot.actuate.metrics.writer.Delta;
import org.springframework.boot.actuate.metrics.writer.MetricWriter;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.sleuth.Sampler;
import org.springframework.cloud.sleuth.sampler.AlwaysSampler;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.servlet.Filter;
import java.io.IOException;

/**
 * @Author: Josiah Adetayo
 * @Email: j.adetayo@bcs.org.uk, josiah.adetayo@cwg-plc.com
 * @Date: 5/8/18
 */
@EnableBinding(Source.class)
@EnableCircuitBreaker
@EnableResourceServer
@SpringBootApplication
public class OrganizationApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrganizationApplication.class, args);
    }

    @Bean
    public Sampler sampler() {
        return new AlwaysSampler();
    }

    @Bean
    public Filter userContextFilter() {
        return new UserContextFilter();
    }
    /*public UserContextFilter userContextFilter() {
        return new UserContextFilter();
    }*/

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        Resource resource = new ClassPathResource("publickey.txt");
        String publicKey;

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
