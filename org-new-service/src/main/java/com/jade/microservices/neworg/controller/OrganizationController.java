package com.jade.microservices.neworg.controller;

import com.jade.microservices.neworg.model.Organization;
import com.jade.microservices.neworg.service.OrganizationService;
import com.jade.microservices.neworg.util.UserContextHolder;
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

    @Autowired private OrganizationService orgService;

    @RequestMapping(value="/",method = RequestMethod.GET)
    public List<Organization> getOrganizations() {
        log.info("*** getOrganizations *** {}", UserContextHolder.getContext().getCorrelationId());
        return orgService.getRepository().findAll();
    }

    @RequestMapping(value="/{organizationId}",method = RequestMethod.GET)
    public Organization getOrganization( @PathVariable("organizationId") String organizationId) {

        Organization org = orgService.getRepository().findOne(organizationId);
        org.setContactName( "NEW::" + org.getContactName() );

        return org;
    }

    @RequestMapping(value="{organizationId}",method = RequestMethod.PUT)
    public String updateOrganization( @PathVariable("organizationId") String organizationId) {
        Organization organization = orgService.getRepository().findOne(organizationId);
        orgService.updateOrganization(organization);
        return String.format("The Organization was successfully updated!");
    }

    @RequestMapping(value="/",method = RequestMethod.POST)
    public void saveOrganization(@RequestBody Organization organization) {
        orgService.createNewOrganization(organization);
    }

    @RequestMapping(value="/{organizationId}",method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String deleteOrganization( @PathVariable("organizationId") String organizationId) {
        Organization organization = orgService.getRepository().findOne(organizationId);
        orgService.deleteOrganization(organization);
        return String.format("This is the Delete");
    }

}
