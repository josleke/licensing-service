package com.jade.microservices.book.ms.service;

import com.jade.microservices.book.ms.model.OrganizationDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @Author: Josiah Adetayo
 * @Email: j.adetayo@bcs.org.uk, josiah.adetayo@cwg-plc.com
 * @Date: 5/9/18
 */
@FeignClient("organizationms") // Identify your service to Feign using the FeignClient Annotation.
public interface OrganizationFeignClient {

    //The path and action to your endpoint is defined using the @RequestMapping annotation.
    @RequestMapping(method = RequestMethod.GET,
                    value = "/v1/organizations/{organizationId}",
                    consumes = "application/json")
    OrganizationDto getOrganization(@PathVariable("organizationId")String organizationId);

}
