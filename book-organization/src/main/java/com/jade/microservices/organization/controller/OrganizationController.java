package com.jade.microservices.organization.controller;

import com.jade.microservices.organization.model.Organization;
import com.jade.microservices.organization.service.OrganizationService;
import com.jade.microservices.organization.util.UserContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: Josiah Adetayo
 * @Email: j.adetayo@bcs.org.uk, josiah.adetayo@cwg-plc.com
 * @Date: 5/9/18
 */
@Slf4j
@RestController
@RequestMapping(value="v1/organizations")
public class OrganizationController {

    @Autowired private OrganizationService organizationService;

    @RequestMapping(value="/",method = RequestMethod.GET)
    public List<Organization> getOrganizations() {
        log.info("*** getOrganizations *** {}", UserContextHolder.getContext().getCorrelationId());
        return organizationService.getRepository().findAll();
    }

    @RequestMapping(value="/{organizationId}",method = RequestMethod.GET)
    public Organization getLicenses( @PathVariable("organizationId") String organizationId) {

        return organizationService.getOrganization(organizationId);
//        return organizationService.getRepository().findOne(organizationId);
    }

    @RequestMapping(value="{organizationId}",method = RequestMethod.PUT)
    public String updateLicenses( @PathVariable("organizationId") String organizationId) {
        Organization organization = organizationService.getRepository().findOne(organizationId);
        organizationService.updateOrganization(organization);
        return String.format("The Organization was successfully updated!");
    }

    @RequestMapping(value="/",method = RequestMethod.POST)
    public void saveLicenses(@RequestBody Organization organization) {
        organizationService.createNewOrganization(organization);
    }

    @RequestMapping(value="/{organizationId}",method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String deleteLicenses( @PathVariable("organizationId") String organizationId) {
        Organization organization = organizationService.getRepository().findOne(organizationId);
        organizationService.deleteOrganization(organization);
        return String.format("This is the Delete");
    }

}
