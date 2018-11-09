package com.jade.microservices.gateway.filters;

import com.jade.microservices.gateway.filters.util.FilterUtils;
import com.jade.microservices.gateway.model.AbTestingRoute;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.ProxyRequestHelper;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @Author: Josiah Adetayo
 * @Email: j.adetayo@bcs.org.uk, josiah.adetayo@cwg-plc.com
 * @Date: 5/15/18
 */
@Slf4j
@Component
public class SpecialRouteFilter extends ZuulFilter {

    private static final int FILTER_ORDER = 1;
    private static final boolean SHOULD_FILTER = true;
    // The helper variable is an instance variable of type ProxyRequestHelper class. This is a Spring Cloud class
    // with helper functions for proxying service requests
    private ProxyRequestHelper helper = new ProxyRequestHelper();

    // Commonly used functions that are used across all your filters have been encapsulated in the FilterUtils class.
    @Autowired
    FilterUtils filterUtils;

    @Autowired RestTemplate restTemplate;

    @Override
    public String filterType() {
        return FilterUtils.ROUTE_FILTER_TYPE;
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

        /*RequestContext ctx = RequestContext.getCurrentContext();

        // Executes call to SpecialRoutes service to determine if there is a routing record for this org
        AbTestingRoute abTestRoute = getAbRoutingInfo(filterUtils.getServiceId());

        // The useSpecialRoute() method will take the weight of the route, generate a random number,
        // and determine if you’re going to forward the request onto the alternative service.
        if(abTestRoute != null && useSpecialRoute(abTestRoute)) {

            // If there’s a routing record, build the full URL (with path) to the service location
            // specified by the special routes service
            String route = buildRouteString(ctx.getRequest().getRequestURI(), abTestRoute.getEndpoint(),
                    ctx.get("serviceId").toString());

            // The forwardToSpecialRoute() method does the work of forwarding onto the alternative service.
            forwardToSpecialRoute(route);
        }*/

        return null;
    }

    /**
     * Utility Methods
     */

    private AbTestingRoute getAbRoutingInfo(String serviceName) {

        ResponseEntity<AbTestingRoute> restExchange = null;

        try {
            restExchange = restTemplate.exchange("http://specialroutems/v1/specialroute/abtesting/{serviceName}",
                    HttpMethod.GET, null, AbTestingRoute.class, serviceName);
        }catch (HttpClientErrorException ex) {
            if (ex.getStatusCode()== HttpStatus.NOT_FOUND) {
                return null;
            }
            throw ex;
        }

        log.error("*** AbTestingRoute {}", restExchange.getBody());

        return restExchange.getBody();
    }

    private boolean useSpecialRoute(AbTestingRoute abTestRoute) {

        Random random = new Random();

        // Checks to see if the route is even active
        if (abTestRoute.getActive().equals("N"))
            return false;

        // Determines whether you should use the alternative service route
        int value = random.nextInt((10 - 1) + 1) + 1;

        if (abTestRoute.getWeight()<value)
            return true;

        return false;
    }

    private String buildRouteString(String uri, String endpoint, String serviceName) {
//        int index = uri.indexOf(serviceName); // The Ideal case
        int index = uri.indexOf("v1"); // The Ideal case

        System.err.println("uri: "+uri+" \nendpoint "+endpoint+"\nserviceName "+serviceName+"\nindex: "+index);
//        String strippedRoute = uri.substring(index + serviceName.length());
        String strippedRoute = uri.substring(index);
        System.out.println("Target route: " + String.format("%s/%s", endpoint, strippedRoute));
        return String.format("%s/%s", endpoint, strippedRoute);
    }

    private void forwardToSpecialRoute(String route) {

        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();

        MultiValueMap<String, String> headers = helper.buildZuulRequestHeaders(request);
        MultiValueMap<String, String> params = helper.buildZuulRequestQueryParams(request);

        String verb = getVerb(request);
        //Makes a copy of the HTTP Body that will be forwarded onto the alternative service
        InputStream requestEntity = getRequestBody(request);
        if (request.getContentLength() < 0) context.setChunkedRequestBody();

        this.helper.addIgnoredHeaders();
        CloseableHttpClient httpClient = null;
        HttpResponse response = null;

        try {
            // Invokes the alternative service using the forward helper method (not shown)
            httpClient = HttpClients.createDefault();

            response = forward(
                    httpClient,
                    verb,
                    route,
                    request,
                    headers,
                    params,
                    requestEntity);

            setResponse(response);
        }catch (Exception ex ) {//Removed for conciseness
         }
    }


    private String getVerb(HttpServletRequest request) { return request.getMethod().toUpperCase(); }

    private HttpHost getHttpHost(URL host) {
        return new HttpHost(host.getHost(), host.getPort(), host.getProtocol());
    }

    private Header[] convertHeaders(MultiValueMap<String, String> headers) {
        List<Header> list = new ArrayList<>();
        for (String name : headers.keySet()) {
            for (String value : headers.get(name)) {
                list.add(new BasicHeader(name, value));
            }
        }
        return list.toArray(new BasicHeader[0]);
    }

    private HttpResponse forwardRequest(HttpClient httpclient,
                                        HttpHost httpHost,
                                        HttpRequest httpRequest) throws IOException {
        return httpclient.execute(httpHost, httpRequest);
    }

    private MultiValueMap<String, String> revertHeaders(Header[] headers) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        for (Header header : headers) {
            String name = header.getName();
            if (!map.containsKey(name)) {
                map.put(name, new ArrayList<>());
            }
            map.get(name).add(header.getValue());
        }
        return map;
    }

    private InputStream getRequestBody(HttpServletRequest request) {
        InputStream requestEntity = null;
        try {
            requestEntity = request.getInputStream();
        }
        catch (IOException ex) {
            // no requestBody is ok.
            log.error("*** ERROR ECOUNTERED RETRIEVING THE BODY ***");
        }
        return requestEntity;
    }

    private void setResponse(HttpResponse response) throws IOException {
        this.helper.setResponse(response.getStatusLine().getStatusCode(),
                response.getEntity() == null ? null : response.getEntity().getContent(),
                revertHeaders(response.getAllHeaders()));
    }

    private HttpResponse forward(HttpClient httpclient, String verb, String uri,
                                 HttpServletRequest request, MultiValueMap<String, String> headers,
                                 MultiValueMap<String, String> params, InputStream requestEntity)
            throws Exception {

        Map<String, Object> info = this.helper.debug(verb, uri, headers, params, requestEntity);
        URL host = new URL( uri );
        HttpHost httpHost = getHttpHost(host);

        HttpRequest httpRequest;
        int contentLength = request.getContentLength();

        InputStreamEntity entity = new InputStreamEntity(requestEntity, contentLength,
                request.getContentType() != null
                        ? ContentType.create(request.getContentType()) : null);

        switch (verb.toUpperCase()) {
            case "POST":
                HttpPost httpPost = new HttpPost(uri);
                httpRequest = httpPost;
                httpPost.setEntity(entity);
                break;
            case "PUT":
                HttpPut httpPut = new HttpPut(uri);
                httpRequest = httpPut;
                httpPut.setEntity(entity);
                break;
            case "PATCH":
                HttpPatch httpPatch = new HttpPatch(uri );
                httpRequest = httpPatch;
                httpPatch.setEntity(entity);
                break;
            default:
                httpRequest = new BasicHttpRequest(verb, uri);

        }

        try {
            httpRequest.setHeaders(convertHeaders(headers));
            HttpResponse zuulResponse = forwardRequest(httpclient, httpHost, httpRequest);

            return zuulResponse;
        }
        finally {
        }
    }


}
