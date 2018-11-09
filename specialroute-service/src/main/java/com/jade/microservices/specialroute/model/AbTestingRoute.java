package com.jade.microservices.specialroute.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @Author: Josiah Adetayo
 * @Email: j.adetayo@bcs.org.uk, josiah.adetayo@cwg-plc.com
 * @Date: 5/16/18
 */
@Entity
@Table(name = "abtesting")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AbTestingRoute implements Serializable {

    @Id
    @Column(name = "service_name", nullable = false)
    private String serviceName;

    @Column(name = "active", nullable = false)
    private String active;

    @Column(name = "endpoint", nullable = false)
    private String endpoint;

    @Column(name = "weight")
    private int weight;

}
