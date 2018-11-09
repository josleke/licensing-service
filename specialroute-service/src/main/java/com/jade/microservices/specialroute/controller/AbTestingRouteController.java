package com.jade.microservices.specialroute.controller;

import com.jade.microservices.specialroute.model.AbTestingRoute;
import com.jade.microservices.specialroute.service.AbTestingRouteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Josiah Adetayo
 * @Email: j.adetayo@bcs.org.uk, josiah.adetayo@cwg-plc.com
 * @Date: 5/16/18
 */
@Slf4j
@RestController
@RequestMapping(value = "v1/specialroute")
public class AbTestingRouteController {

    @Autowired private AbTestingRouteService abTestingRouteService;

    @RequestMapping(value="abtesting/{serviceName}",method = RequestMethod.GET)
    public AbTestingRoute abstestings(@PathVariable("serviceName") String serviceName) {

        log.debug("**** In the Special Route with the service name {}", serviceName);
        return abTestingRouteService.getRoute(serviceName);
    }
}
