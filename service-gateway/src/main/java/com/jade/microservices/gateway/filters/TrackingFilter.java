package com.jade.microservices.gateway.filters;

import com.jade.microservices.gateway.filters.util.FilterUtils;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @Author: Josiah Adetayo
 * @Email: j.adetayo@bcs.org.uk, josiah.adetayo@cwg-plc.com
 * @Date: 5/14/18
 * Note: All Zuul filters must extend the ZuulFilter class and override four methods: filterType(), filterOrder(),
 * shouldFilter(), and run().
 */
@Slf4j
@Component
public class TrackingFilter extends ZuulFilter {

    private static final int FILTER_ORDER = 1;
    private static final boolean SHOULD_FILTER = true;

    // Commonly used functions that are used across all your filters have been encapsulated in the FilterUtils class.
    @Autowired FilterUtils filterUtils;

    @Override
    public String filterType() {
        return FilterUtils.PRE_FILTER_TYPE;
    }

    @Override
    public int filterOrder() {
        return FILTER_ORDER;
    }

    // The shouldFilter() method returns a Boolean indicating whether or not the filter should be active.
    @Override
    public boolean shouldFilter() {
        return SHOULD_FILTER;
    }

    /**
     * The run() method is the code that is executed every time a service passes through the
     * filter. In your run() function, you check to see if the tmxcorrelation- id is present and
     * if it isn’t, you generate a correlation value and set the tmx-correlation-id HTTP
     * @return
     */
    @Override
    public Object run() {
        if(isCorrelationIdPresent()) {
            log.info("tmx-correlation-id found in tracking filter: {}.", filterUtils.getCorrelationId());
        }else{
            filterUtils.setCorrelationId(generateCorrelationId());
            log.info("tmx-correlation-id generated in tracking filter: {}.", filterUtils.getCorrelationId());
        }

        RequestContext requestCxt = RequestContext.getCurrentContext();
        log.info("Processing request for incoming request with URL: {}.", requestCxt.getRequest().getRequestURI());
        return null;
    }

    /**
     * Utility Methods
     */

    private boolean isCorrelationIdPresent() {
        return (filterUtils.getCorrelationId() != null);
    }

    // The helper methods that actually check if the tmx-correlation-id is present and can also generate a
    // correlation ID GUID value
    private String generateCorrelationId() {
        return UUID.randomUUID().toString();
    }
}
