package com.jade.microservices.book.ms.config.service;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @Author: Josiah Adetayo
 * @Email: j.adetayo@bcs.org.uk, josiah.adetayo@cwg-plc.com
 * @Date: 9/18/18
 */
@Data
@Component
public class ServiceConfig implements Serializable {

    @Value("${default.comment}")
    private String exampleComment;

    @Value("${redis.server}")
    private String redisHost;

    @Value("${redis.port}")
    private int redisPort;



}
