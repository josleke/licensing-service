package com.jade.microservices.book.ms.event.channel;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * @Author: Josiah Adetayo
 * @Email: j.adetayo@bcs.org.uk, josiah.adetayo@cwg-plc.com
 * @Date: 9/18/18
 */
public interface CustomChannel {

    @Input("inboundOrgChanges") // a method-level annotation that defines the name of the channel
    SubscribableChannel organization(); // Each channel exposed through the @Input annotation must return a
                                       // SubscribableChannel class
/*
    @Output("outboundOrgChanges") // or @OutputChannel("outboundOrgChanges")
    MessageChannel outBoundOrgChange();*/

}
