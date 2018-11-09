package com.jade.microservices.neworg.service;

import com.jade.microservices.neworg.model.Organization;
import com.jade.microservices.neworg.repository.OrganizationRepository;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public OrganizationServiceImpl(OrganizationRepository repository) {
        this.repository = repository;
    }

    @Override
    public OrganizationRepository getRepository() {
        return repository;
    }

    @Override
    public Organization createNewOrganization(Organization organization) {
        Organization savedOrganization = null;

        try {
            organization.setOrganizationId(UUID.randomUUID().toString());
            savedOrganization = repository.save(organization);
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
        }catch (Exception ex) {
            log.error("*** The  Organization details could not be updated ***");
        }

        return updatedOrganization;
    }

    @Override
    public boolean deleteOrganization(Organization organization) {

        try {
            repository.delete(organization);
        }catch (Exception ex) {
            log.error("*** The Selected Organization could not be deleted ***");
            return false;
        }
        return true;

    }
}
