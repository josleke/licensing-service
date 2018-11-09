package com.jade.microservices.gateway.filters;

import com.jade.microservices.gateway.filters.util.FilterUtils;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.stereotype.Component;

/**
 * @Author: Josiah Adetayo
 * @Email: j.adetayo@bcs.org.uk, josiah.adetayo@cwg-plc.com
 * @Date: 5/15/18
 */
@Slf4j
@Component
public class ResponseFilter extends ZuulFilter {

    private static final int FILTER_ORDER = 1;
    private static final boolean SHOULD_FILTER = true;

    // Commonly used functions that are used across all your filters have been encapsulated in the FilterUtils class.
//    @Autowired FilterUtils filterUtils;
    @Autowired Tracer tracer;

    @Override
    public String filterType() {
        return FilterUtils.POST_FILTER_TYPE;
    }

    @Override
    public int filterOrder() {
        return FILTER_ORDER;
    }

    @Override
    public boolean shouldFilter() {
        return SHOULD_FILTER;
    }

    @Override
    public Object run() {

        RequestContext requestCxt = RequestContext.getCurrentContext();
        log.info("Adding the correlation id to the outbound headers: {}.", tracer.getCurrentSpan().traceIdString());

        requestCxt.getResponse().addHeader(FilterUtils.CORRELATION_ID, tracer.getCurrentSpan().traceIdString());

        /*log.info("Adding the correlation id to the outbound headers: {}.", filterUtils.getCorrelationId());

        requestCxt.getResponse().addHeader(FilterUtils.CORRELATION_ID, filterUtils.getCorrelationId());

        */

        log.info("Completing outgoing request for {}.", requestCxt.getRequest().getRequestURI());

        return null;
    }
}
