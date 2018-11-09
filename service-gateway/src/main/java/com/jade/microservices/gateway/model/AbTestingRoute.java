package com.jade.microservices.gateway.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: Josiah Adetayo
 * @Email: j.adetayo@bcs.org.uk, josiah.adetayo@cwg-plc.com
 * @Date: 5/16/18
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AbTestingRoute implements Serializable {

    private String serviceName;
    private String active;
    private String endpoint;
    private int weight;

    @Override
    public String toString() {
        return "AbTestingRoute{" +
                "serviceName='" + serviceName + '\'' +
                ", active='" + active + '\'' +
                ", endpoint='" + endpoint + '\'' +
                ", weight=" + weight +
                '}';
    }
}
