package com.jade.microservices.book.ms.event.handler;

import com.jade.microservices.book.ms.event.channel.CustomChannel;
import com.jade.microservices.book.ms.event.model.OrganizationChangeModel;
import com.jade.microservices.book.ms.repository.OrganizationRedisRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

/**
 * @Author: Josiah Adetayo
 * @Email: j.adetayo@bcs.org.uk, josiah.adetayo@cwg-plc.com
 * @Date: 9/18/18
 */
@Slf4j
@EnableBinding(CustomChannel.class)
public class OrganizationChangeHandler {

    @Autowired private OrganizationRedisRepository organizationRedisRepository;

    // With the @StreamListener annotation, you passed in the name of
    // your channel, “inboundOrgChanges”, instead of using Sink.INPUT
    @StreamListener("inboundOrgChanges")
    public void logConsumer(OrganizationChangeModel orgChange) {

        switch (orgChange.getAction()) {
            case "UPDATE": {
                log.debug("Received an UPDATE event for organization Id: {}\n {}", orgChange.getOrgId(), orgChange);
                organizationRedisRepository.deleteOrganization(orgChange.getOrgId());
            }
            break;
            case "DELETE":{
                log.debug("Received a DELETE event for organization Id: {}\n {}", orgChange.getOrgId(), orgChange);
                organizationRedisRepository.deleteOrganization(orgChange.getOrgId());
            }
            break;
            default:
                log.error("Received an UNKNOWN event for organization Id: {}\n {}", orgChange.getOrgId(), orgChange);
        }

    }
}
