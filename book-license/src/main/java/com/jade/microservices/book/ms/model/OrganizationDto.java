package com.jade.microservices.book.ms.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: Josiah Adetayo
 * @Email: j.adetayo@bcs.org.uk, josiah.adetayo@cwg-plc.com
 * @Date: 5/9/18
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationDto implements Serializable {

    private String organizationId;
    private String name;
    private String contact_name;
    private String contact_email;
    private String contact_phone;

}
