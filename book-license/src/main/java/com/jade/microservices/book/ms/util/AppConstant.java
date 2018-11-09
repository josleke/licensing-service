package com.jade.microservices.book.ms.util;

/**
 * @Author: Josiah Adetayo
 * @Email: j.adetayo@bcs.org.uk, josiah.adetayo@cwg-plc.com
 * @Date: 5/9/18
 */
public interface AppConstant {

    interface ClientType {
        String DISCOVERY_CLIENT = "dc";
        String RIBBON_BACKED_REST_TEMPLATE = "rt";
        String FEIGN_CLIENT = "fc";

    }
}
