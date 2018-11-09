package com.jade.microservices.organization.repository;

import com.jade.microservices.organization.model.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author: Josiah Adetayo
 * @Email: j.adetayo@bcs.org.uk, josiah.adetayo@cwg-plc.com
 * @Date: 5/9/18
 */
public interface OrganizationRepository extends JpaRepository<Organization, String> {
}
