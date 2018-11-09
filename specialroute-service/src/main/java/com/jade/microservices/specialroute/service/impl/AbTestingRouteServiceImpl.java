package com.jade.microservices.specialroute.service.impl;

import com.jade.microservices.specialroute.model.AbTestingRoute;
import com.jade.microservices.specialroute.repository.AbTestingRouteRepository;
import com.jade.microservices.specialroute.service.AbTestingRouteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: Josiah Adetayo
 * @Email: j.adetayo@bcs.org.uk, josiah.adetayo@cwg-plc.com
 * @Date: 5/16/18
 */
@Slf4j
@Service
public class AbTestingRouteServiceImpl implements AbTestingRouteService {

    private AbTestingRouteRepository repository;

    @Autowired
    public AbTestingRouteServiceImpl(AbTestingRouteRepository repository) {
        this.repository = repository;
    }

    @Override
    public AbTestingRouteRepository getRepository() {
        return repository;
    }

    @Override
    public AbTestingRoute createNewAbTestingRoute(AbTestingRoute abTestingRoute) {
        AbTestingRoute savedAbTestingRoute = null;

        try {
            savedAbTestingRoute = repository.save(abTestingRoute);
        }catch (Exception ex) {
            log.error("*** The new AbTestingRoute could not be saved ***");
        }

        return savedAbTestingRoute;
    }

    @Override
    public AbTestingRoute updateAbTestingRoute(AbTestingRoute abTestingRoute) {
        AbTestingRoute updatedAbTestingRoute = null;

        try {
            updatedAbTestingRoute = repository.save(abTestingRoute);
        }catch (Exception ex) {
            log.error("*** The  AbTestingRoute details could not be updated ***");
        }

        return updatedAbTestingRoute;
    }

    @Override
    public boolean deleteAbTestingRoute(AbTestingRoute abTestingRoute) {
        try {
            repository.delete(abTestingRoute);
        }catch (Exception ex) {
            log.error("*** The Selected AbTestingRoute could not be deleted ***");
            return false;
        }
        return true;
    }

    @Override
    public AbTestingRoute getRoute(String serviceName) {
        return repository.findOne(serviceName);
    }
}
