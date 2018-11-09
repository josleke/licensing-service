package com.jade.microservices.book.ms.event.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: Josiah Adetayo
 * @Email: j.adetayo@bcs.org.uk, josiah.adetayo@cwg-plc.com
 * @Date: 9/17/18
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationChangeModel implements Serializable {

    private String modelName;
    private String action;
    private String orgId;
    private String correlationId;


    @Override
    public String toString() {
        return "OrganizationChangeModel{" +
                "modelName='" + modelName + '\'' +
                ", action='" + action + '\'' +
                ", orgId='" + orgId + '\'' +
                ", correlationId='" + correlationId + '\'' +
                '}';
    }
}
