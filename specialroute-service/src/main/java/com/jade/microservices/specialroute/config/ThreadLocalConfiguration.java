package com.jade.microservices.specialroute.config;

import com.netflix.hystrix.strategy.HystrixPlugins;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
import com.netflix.hystrix.strategy.eventnotifier.HystrixEventNotifier;
import com.netflix.hystrix.strategy.executionhook.HystrixCommandExecutionHook;
import com.netflix.hystrix.strategy.metrics.HystrixMetricsPublisher;
import com.netflix.hystrix.strategy.properties.HystrixPropertiesStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @Author: Josiah Adetayo
 * @Email: j.adetayo@bcs.org.uk, josiah.adetayo@cwg-plc.com
 * @Date: 5/11/18
 */
@Slf4j
@Configuration
public class ThreadLocalConfiguration {

    // When the configuration object is constructed it will autowire in the existing HystrixConcurrencyStrategy.
    @Autowired(required = false) private HystrixConcurrencyStrategy existingConcurrencyStrategy;

    @PostConstruct
    public void init() {
        log.debug("Initializing ThreadLocalConfiguration for SpecialRoute ");

        // Keeps references of existing Hystrix plugins.
        HystrixEventNotifier eventNotifier = HystrixPlugins.getInstance().getEventNotifier();
        HystrixMetricsPublisher metricsPublisher = HystrixPlugins.getInstance().getMetricsPublisher();
        HystrixPropertiesStrategy propertiesStrategy = HystrixPlugins.getInstance().getPropertiesStrategy();
        HystrixCommandExecutionHook commandExecutionHook = HystrixPlugins.getInstance().getCommandExecutionHook();

        // Because you’re registering a new concurrency strategy, we grabbed all the other
        // Hystrix components and then reset the Hystrix plugin.
        HystrixPlugins.reset();
        HystrixPlugins hystrixPlugins = HystrixPlugins.getInstance();

        // We now register your HystrixConcurrencyStrategy (ThreadLocalAwareStrategy) with the Hystrix plugin.
        hystrixPlugins.registerConcurrencyStrategy(new ThreadLocalAwareStrategy(existingConcurrencyStrategy));
        hystrixPlugins.registerEventNotifier(eventNotifier); // Reregister all the Hystrix components used by the Hystrix plugin
        hystrixPlugins.registerMetricsPublisher(metricsPublisher);
        hystrixPlugins.registerPropertiesStrategy(propertiesStrategy);
        hystrixPlugins.registerCommandExecutionHook(commandExecutionHook);
    }

}
