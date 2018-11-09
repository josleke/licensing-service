package com.jade.microservices.book.ms.service;

import com.jade.microservices.book.ms.model.OrganizationDto;
import com.jade.microservices.book.ms.repository.OrganizationRedisRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Component;

/**
 * @Author: Josiah Adetayo
 * @Email: j.adetayo@bcs.org.uk, josiah.adetayo@cwg-plc.com
 * @Date: 5/9/18
 */
@Slf4j
@Component
public class OrganizationRestTemplateClient {

//    @Autowired RestTemplate restTemplate;
    @Autowired OAuth2RestTemplate restTemplate;
    @Autowired OrganizationRedisRepository redisRepository;
    @Autowired Tracer tracer;


    private OrganizationDto checkRedisCache(String organizationId) {

        // For your custom span, create a new span called readLicensingDataFromRedis
        Span newSpan = tracer.createSpan("readLicenseDataFromRedis");
        try {

            return redisRepository.findOrganizationDto(organizationId);

        } catch (Exception ex) {
            log.error("Error encountered while trying to retrieve organization {} check Redis Cache. " +
                    " Exception {}", organizationId, ex);
        }finally { //Close the span out with a finally block
            // In this class you provide the name of the service that’s going to be captured by Zipkin
            newSpan.tag("peer.service", "redis");
            //Log an event to tell Spring Cloud Sleuth that it should capture the time when the call is complete
            newSpan.logEvent(Span.CLIENT_RECV);
            // Close out the trace. If you don’t call the close() method, you’ll get error messages in the logs
            //i ndicating that a span has been left open
            tracer.close(newSpan);
        }

        return null;
    }

    private void cacheOrganizationObject(OrganizationDto organizationDto) {
        try {
            redisRepository.saveOrganization(organizationDto);
        }catch (Exception ex) {
            log.error("Unable to cache organization {} in Redis ", organizationDto.getOrganizationId(), ex);
        }
    }



    public OrganizationDto getOrganization(String organizationId) {

        log.debug("In Licensing Service. getOrganization: {}", organizationId);

        /**
         * If you can’t retrieve data from Redis, you’ll call out the organization service
         * to retrieve the data from the source database
        */
        OrganizationDto organizationDto = checkRedisCache(organizationId);

        if(organizationDto != null) {
            log.debug("I have successfully retrieved an organization {} from the redis cache: {}"
                    , organizationId, organizationDto);

            return organizationDto;
        }else log.debug("Unable to locate organization from the redis cache: {}.", organizationId);

        //The target URL of the Eureka service ID is used When using a Ribbon-back RestTemplate
        ResponseEntity< OrganizationDto > restExchange =
                restTemplate.exchange(
//                        "http://organizationms/v1/organizations/{organizationId}",
                        "http://localhost:5555/api/organization/v1/organizations/{organizationId}",
                        HttpMethod.GET,
                        null, OrganizationDto.class, organizationId);

        organizationDto = restExchange.getBody();

        if(organizationDto!= null) {
            /*Save the record from cache*/
            cacheOrganizationObject(organizationDto);
        }

        return organizationDto;
//        return restExchange.getBody();

    }
}
