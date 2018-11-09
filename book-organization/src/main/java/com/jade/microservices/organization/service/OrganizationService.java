package com.jade.microservices.organization.service;

import com.jade.microservices.organization.model.Organization;
import com.jade.microservices.organization.repository.OrganizationRepository;

/**
 * @Author: Josiah Adetayo
 * @Email: j.adetayo@bcs.org.uk, josiah.adetayo@cwg-plc.com
 * @Date: 5/9/18
 */
public interface OrganizationService {

    OrganizationRepository getRepository();

    Organization getOrganization(String organizationId);

    Organization createNewOrganization(Organization organization);

    Organization updateOrganization(Organization organization);

    boolean deleteOrganization(Organization organization);
}
