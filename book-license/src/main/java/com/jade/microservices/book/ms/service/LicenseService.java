package com.jade.microservices.book.ms.service;

import com.jade.microservices.book.ms.model.License;
import com.jade.microservices.book.ms.repository.LicenseRepository;

import java.util.List;

/**
 * @Author: Josiah Adetayo
 * @Email: j.adetayo@bcs.org.uk, josiah.adetayo@cwg-plc.com
 * @Date: 5/8/18
 */
public interface LicenseService {

    LicenseRepository getLicenseRepository();

    License createNewLicense(License license);

    License updateLicense(License license);

    boolean deleteLicense(License license);

    License getLicenseByOrganizationIdAndLicenseId(String organizationId, String licenseId);

    List<License> getLicensesByOrganizationId(String organizationId);

    License getLicense(String organizationId, String licenseId, String clientType);
}
