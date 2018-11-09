package com.jade.microservices.book.ms.repository;

import com.jade.microservices.book.ms.model.OrganizationDto;

/**
 * @Author: Josiah Adetayo
 * @Email: j.adetayo@bcs.org.uk, josiah.adetayo@cwg-plc.com
 * @Date: 9/18/18
 */
public interface OrganizationRedisRepository {

    void saveOrganization(OrganizationDto organizationDto);
    void updateOrganization(OrganizationDto organizationDto);
    void deleteOrganization(String organizationId);
    OrganizationDto findOrganizationDto(String organizationId);

}
