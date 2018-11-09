package com.jade.microservices.specialroute.service;

import com.jade.microservices.specialroute.model.AbTestingRoute;
import com.jade.microservices.specialroute.repository.AbTestingRouteRepository;

/**
 * @Author: Josiah Adetayo
 * @Email: j.adetayo@bcs.org.uk, josiah.adetayo@cwg-plc.com
 * @Date: 5/16/18
 */
public interface AbTestingRouteService {

    AbTestingRouteRepository getRepository();

    AbTestingRoute createNewAbTestingRoute(AbTestingRoute abTestingRoute);

    AbTestingRoute updateAbTestingRoute(AbTestingRoute abTestingRoute);

    AbTestingRoute getRoute(String serviceName);

    boolean deleteAbTestingRoute(AbTestingRoute abTestingRoute);

}
