package com.jade.microservices.book.ms.service.impl;

import com.jade.microservices.book.ms.config.service.ServiceConfig;
import com.jade.microservices.book.ms.model.License;
import com.jade.microservices.book.ms.model.OrganizationDto;
import com.jade.microservices.book.ms.repository.LicenseRepository;
import com.jade.microservices.book.ms.service.LicenseService;
import com.jade.microservices.book.ms.service.OrganizationDiscoveryClient;
import com.jade.microservices.book.ms.service.OrganizationFeignClient;
import com.jade.microservices.book.ms.service.OrganizationRestTemplateClient;
import com.jade.microservices.book.ms.util.AppConstant;
import com.jade.microservices.book.ms.util.UserContextHolder;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * @Author: Josiah Adetayo
 * @Email: j.adetayo@bcs.org.uk, josiah.adetayo@cwg-plc.com
 * @Date: 5/8/18
 */
@Slf4j
@Service
public class LicenseServiceImpl implements LicenseService {

    private LicenseRepository licenseRepository;
    private ServiceConfig serviceConfig;
    // One is actually needed
    private OrganizationDiscoveryClient discoveryClient;
    private OrganizationRestTemplateClient restTemplateClient;
    private OrganizationFeignClient feignClient;

    @Autowired
    public LicenseServiceImpl(LicenseRepository licenseRepository, ServiceConfig serviceConfig,
                              OrganizationDiscoveryClient discoveryClient,
                              OrganizationRestTemplateClient restTemplateClient,
                              OrganizationFeignClient feignClient) {
        this.licenseRepository = licenseRepository;
        this.serviceConfig = serviceConfig;
        this.discoveryClient = discoveryClient;
        this.restTemplateClient = restTemplateClient;
        this.feignClient = feignClient;
    }

    @Override
    public LicenseRepository getLicenseRepository() {
        return licenseRepository;
    }

    @Override
    public License createNewLicense(License license) {

        License savedLicense = null;

        try {
            license.setLicenseId(UUID.randomUUID().toString());
            license.setComment(serviceConfig.getExampleComment());
            savedLicense = licenseRepository.save(license);
        }catch (Exception ex) {
            log.error("*** The new License could not be saved ***");
        }

        return savedLicense;
    }

    @Override
    public License updateLicense(License license) {
        License updatedLicense = null;

        try {
            updatedLicense = licenseRepository.save(license);
        }catch (Exception ex) {
            log.error("*** The Updated License could not be updated ***");
        }

        return updatedLicense;
    }

    @Override
    public boolean deleteLicense(License license) {

        try {
             licenseRepository.delete(license);
        }catch (Exception ex) {
            log.error("*** The Selected License could not be deleted ***");
            return false;
        }
        return true;
    }

    @Override
    public License getLicenseByOrganizationIdAndLicenseId(String organizationId, String licenseId) {
        log.debug("*** getLicenseByOrganizationIdAndLicenseId Correlation id: {}", UserContextHolder.getContext().getCorrelationId());
        return licenseRepository.findByOrOrganizationIdAndLicenseId(organizationId, licenseId);
    }

    @Override
    @HystrixCommand(fallbackMethod = "getFullFallbackLicenseList",
                    threadPoolKey = "getLicensesByOrganizationThreadPool",
                    threadPoolProperties = {
                        @HystrixProperty( name = "coreSize", value = "30"),
                        @HystrixProperty( name = "maxQueueSize", value = "10") },
                    commandProperties = {
                        @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),
                        @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "75"),
                        @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "7000"),
                        @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "15000"),
                        @HystrixProperty(name = "metrics.rollingStats.numBuckets", value = "5")
                    })
    public List<License> getLicensesByOrganizationId(String organizationId) {

        log.info("*** getLicensesByOrganizationId Correlation id: {}", UserContextHolder.getContext().getCorrelationId());
//        randomLongRun();

        return licenseRepository.findByOrOrganizationId(organizationId);
    }

    @Override
    public License getLicense(String organizationId, String licenseId, String clientType) {

        OrganizationDto organizationDto = getOrganizationDto(organizationId, clientType);

        return licenseRepository.findByOrOrganizationIdAndLicenseId(organizationDto.getOrganizationId(), licenseId);

    }

    @HystrixCommand(commandProperties =
            {@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",
                              value = "12000")})
    private OrganizationDto getOrganizationDto(String organizationId, String clientType) {

        switch (clientType) {
            case AppConstant.ClientType.DISCOVERY_CLIENT:
                return discoveryClient.getOrganization(organizationId);
            case AppConstant.ClientType.RIBBON_BACKED_REST_TEMPLATE:
                return restTemplateClient.getOrganization(organizationId);
            case AppConstant.ClientType.FEIGN_CLIENT:
                return feignClient.getOrganization(organizationId);
        }
         return null;
    }

    private void randomLongRun() {
        Random rand = new Random();

        int randomNum = (rand.nextInt((3 - 1) + 1) + 1);

        log.error("Random Count {}", randomNum);

        if(randomNum == 3) sleep();
    }

    private void sleep() {
        try {
            Thread.sleep(11000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private List<License> getFullFallbackLicenseList(String organizationId) {
        List<License> licenseList = new ArrayList<>();

        licenseList.add(License.builder()
                .licenseId("000-000-000")
                .organizationId(organizationId)
                .productName("Sorry no licensing information currently available")
                .licenseAllocate(0)
                .licenseMax(0)
                .comment("")
                .licenseType("")
                .build());

        return licenseList;
    }
}
