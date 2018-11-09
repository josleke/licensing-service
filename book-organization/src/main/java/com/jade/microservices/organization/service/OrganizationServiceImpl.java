package com.jade.microservices.organization.service;

import com.jade.microservices.organization.controller.SimpleSourceBean;
import com.jade.microservices.organization.model.Organization;
import com.jade.microservices.organization.repository.OrganizationRepository;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @Author: Josiah Adetayo
 * @Email: j.adetayo@bcs.org.uk, josiah.adetayo@cwg-plc.com
 * @Date: 5/9/18
 */
@DefaultProperties(
        commandProperties = {
                @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "15000")
        }
)
@Slf4j
@Service
public class OrganizationServiceImpl implements OrganizationService {

    private OrganizationRepository repository;

    private SimpleSourceBean simpleSourceBean;
    private Tracer tracer;

    @Autowired
    public OrganizationServiceImpl(OrganizationRepository repository,
                                   SimpleSourceBean simpleSourceBean,
                                   Tracer tracer) {
        this.repository = repository;
        this.simpleSourceBean = simpleSourceBean;
        this.tracer = tracer;
    }

    @Override
    public OrganizationRepository getRepository() {
        return repository;
    }

    @Override
    public Organization getOrganization(String organizationId) {

        Span newSpan = tracer.createSpan("callFromOrganizationDB");

        log.debug("In the organizationService.getOrg() call");

        try {
            return repository.findOne(organizationId);
        }finally {
            newSpan.tag("peer.service", "mysql");
            newSpan.logEvent(Span.CLIENT_RECV);
            tracer.close(newSpan);
        }
    }

    @Override
    public Organization createNewOrganization(Organization organization) {
        Organization savedOrganization = null;

        try {
            organization.setOrganizationId(UUID.randomUUID().toString());
            savedOrganization = repository.save(organization);

            // Publish Change
            simpleSourceBean.publishOrgChange("SAVE", savedOrganization.getOrganizationId());

        }catch (Exception ex) {
            log.error("*** The new Organization could not be saved ***");
        }

        return savedOrganization;
    }

    @Override
    public Organization updateOrganization(Organization organization) {

        Organization updatedOrganization = null;

        try {
            updatedOrganization = repository.save(organization);

            // Publish Change
            simpleSourceBean.publishOrgChange("UPDATE", updatedOrganization.getOrganizationId());

        }catch (Exception ex) {
            log.error("*** The  Organization details could not be updated ***");
        }

        return updatedOrganization;
    }

    @Override
    public boolean deleteOrganization(Organization organization) {

        try {
            repository.delete(organization);

            // Publish Change
            simpleSourceBean.publishOrgChange("DELETE", organization.getOrganizationId());

        }catch (Exception ex) {
            log.error("*** The Selected Organization could not be deleted ***");
            return false;
        }
        return true;

    }
}
