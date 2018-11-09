package com.jade.microservices.neworg.service;


import com.jade.microservices.neworg.model.Organization;
import com.jade.microservices.neworg.repository.OrganizationRepository;

/**
 * @Author: Josiah Adetayo
 * @Email: j.adetayo@bcs.org.uk, josiah.adetayo@cwg-plc.com
 * @Date: 5/9/18
 */
public interface OrganizationService {

    OrganizationRepository getRepository();

    Organization createNewOrganization(Organization organization);

    Organization updateOrganization(Organization organization);

    boolean deleteOrganization(Organization organization);
}
