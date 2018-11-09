package com.jade.microservices.book.ms.service;

import com.jade.microservices.book.ms.model.OrganizationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @Author: Josiah Adetayo
 * @Email: j.adetayo@bcs.org.uk, josiah.adetayo@cwg-plc.com
 * @Date: 5/9/18
 */
@Component
public class OrganizationDiscoveryClient {

    @Autowired DiscoveryClient discoveryClient;

    public OrganizationDto getOrganization(String organizationId) {

        RestTemplate restTemplate = new RestTemplate();

        // Gets a list of all the instances of organization services
        List<ServiceInstance> instances = discoveryClient.getInstances("organizationms");

        if (instances.size()==0) return null;

        //Retrieves the service endpoint we are going to call
        String serviceUri = String.format("%s/v1/organizations/%s", instances.get(0).getUri().toString(), organizationId);

        //Uses a standard Spring REST Template class to call the service
        ResponseEntity< OrganizationDto > restExchange =
                restTemplate.exchange(
                        serviceUri,
                        HttpMethod.GET,
                        null, OrganizationDto.class, organizationId);

        return restExchange.getBody();

    }
}
