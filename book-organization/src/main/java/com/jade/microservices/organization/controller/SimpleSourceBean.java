package com.jade.microservices.organization.controller;

import com.jade.microservices.organization.model.dto.OrganizationChangeModel;
import com.jade.microservices.organization.util.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

/**
 * @Author: Josiah Adetayo
 * @Email: j.adetayo@bcs.org.uk, josiah.adetayo@cwg-plc.com
 * @Date: 9/17/18
 */
@Slf4j
@Component
public class SimpleSourceBean {

    private Source source;

    @Autowired
    public SimpleSourceBean(Source source) {
        this.source = source;
    }

    public void publishOrgChange(String action, String orgId) {

        log.debug("Send message {}, for organization id: {}", action, orgId);

        OrganizationChangeModel changeModel = OrganizationChangeModel.builder()
                .modelName(OrganizationChangeModel.class.getName())
                .action(action)
                .orgId(orgId)
                .correlationId(UserContext.CORRELATION_ID)
                .build();

        // Ready to publish the message
        source.output()
                .send(MessageBuilder
                        .withPayload(changeModel)
                        .build());


    }

}
