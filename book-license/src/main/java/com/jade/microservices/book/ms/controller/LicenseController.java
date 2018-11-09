package com.jade.microservices.book.ms.controller;

import com.jade.microservices.book.ms.config.service.ServiceConfig;
import com.jade.microservices.book.ms.model.License;
import com.jade.microservices.book.ms.service.LicenseService;
import com.jade.microservices.book.ms.util.UserContextHolder;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @Author: Josiah Adetayo
 * @Email: j.adetayo@bcs.org.uk, josiah.adetayo@cwg-plc.com
 * @Date: 5/8/18
 */
@Slf4j
@RestController
@RequestMapping(value="v1/organizations/{organizationId}/licenses")
public class LicenseController {

    @Autowired
    private LicenseService licenseService;

    @Autowired private ServiceConfig serviceConfig;


    @RequestMapping(value="/",method = RequestMethod.GET)
    public List<License> getLicenses(@PathVariable("organizationId") String organizationId) {

        log.info("*** getLicenses Correlation id: {}", UserContextHolder.getContext().getCorrelationId());
        return licenseService.getLicensesByOrganizationId(organizationId);
    }

    @RequestMapping(value="/{licenseId}",method = RequestMethod.GET)
    public License getLicenses( @PathVariable("organizationId") String organizationId,
                                @PathVariable("licenseId") String licenseId) {

        log.info("*** Inside The LicenseController::getLicenses ***");
        return licenseService.getLicenseByOrganizationIdAndLicenseId(organizationId,licenseId);
    }

    @RequestMapping(value="{licenseId}",method = RequestMethod.PUT)
    public String updateLicenses( @PathVariable("licenseId") String licenseId) {
        License license = licenseService.getLicenseRepository().findOne(licenseId);
        licenseService.updateLicense(license);
        return String.format("The License was successfully updated!");
    }

    @RequestMapping(value="/",method = RequestMethod.POST)
    public void saveLicenses(@RequestBody License license) {
        licenseService.createNewLicense(license);
    }

    @RequestMapping(value="{licenseId}",method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String deleteLicenses( @PathVariable("licenseId") String licenseId) {
        License license = licenseService.getLicenseRepository().findOne(licenseId);
        licenseService.deleteLicense(license);
        return String.format("This is the Delete");
    }

    /**
     * Calling the service with different REST Client
     */
    @RequestMapping(value="/{licenseId}/{clientType}", method = RequestMethod.GET)
    public License getLicensesWithClient(
            @PathVariable("organizationId") String organizationId,
            @PathVariable("licenseId") String licenseId,
            @PathVariable("clientType") String clientType) {

        return licenseService.getLicense(organizationId, licenseId, clientType);
    }


    /**
     * Wraps calls to the helloRemoteServiceCall method with a Hystrix circuit breaker
     */
    @HystrixCommand(threadPoolKey = "helloThreadPool")
    public String helloRemoteServiceCall(String firstName, String lastName){

        /**
         * Uses a decorated RestTemplate class to take a “logical” service ID and Eureka under the covers
         * to look up the physical location of the service
         */
        ResponseEntity<String> restExchange = initRestTemplate().exchange(
                "http://logical-service-id/name/[ca]{firstName}/{lastName}",
                HttpMethod.GET,
                null,
                String.class,
                firstName,
                lastName
        );

        return restExchange.getBody();
    }

   /* @RequestMapping(value = "/{firstName}/{lastName}", method = RequestMethod.GET)
    public String hello( @PathVariable("firstName") String firstName,
                         @PathVariable("lastName") String lastName) {

        return helloRemoteServiceCall(firstName, lastName);
		*//*return String.format("{\"message\":\"Hello %s %s\"}",
				firstName, lastName);*//*
    }*/

    public RestTemplate initRestTemplate() {
        return new RestTemplate();
    }






}
