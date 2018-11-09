package com.jade.microservices.book.ms.repository;

import com.jade.microservices.book.ms.model.License;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Author: Josiah Adetayo
 * @Email: j.adetayo@bcs.org.uk, josiah.adetayo@cwg-plc.com
 * @Date: 5/8/18
 */
public interface LicenseRepository extends JpaRepository<License, String> {

    List<License> findByOrOrganizationId(String organizationId);

    License findByOrOrganizationIdAndLicenseId(String organizationId, String licenseId);
}
