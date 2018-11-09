package com.jade.microservices.book.ms.config;

import com.jade.microservices.book.ms.util.UserContextHolder;
import com.netflix.hystrix.HystrixThreadPoolKey;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestVariable;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestVariableLifecycle;
import com.netflix.hystrix.strategy.properties.HystrixProperty;

import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Josiah Adetayo
 * @Email: j.adetayo@bcs.org.uk, josiah.adetayo@cwg-plc.com
 * @Date: 5/11/18
 */
public class ThreadLocalAwareStrategy extends HystrixConcurrencyStrategy {

    private HystrixConcurrencyStrategy hystrixConcurrencyStrategy;

    /**
     * Spring Cloud already has a concurrency class defined. Pass the existing concurrency strategy into the class
     * constructor of your HystrixConcurrencyStrategy
     * @param hystrixConcurrencyStrategy
     */
    public ThreadLocalAwareStrategy(HystrixConcurrencyStrategy hystrixConcurrencyStrategy) {
        this.hystrixConcurrencyStrategy = hystrixConcurrencyStrategy;
    }

    @Override
    public ThreadPoolExecutor getThreadPool(HystrixThreadPoolKey threadPoolKey,
                                            HystrixProperty<Integer> corePoolSize,
                                            HystrixProperty<Integer> maximumPoolSize,
                                            HystrixProperty<Integer> keepAliveTime,
                                            TimeUnit unit,
                                            BlockingQueue<Runnable> workQueue) {
        return (Optional.ofNullable(hystrixConcurrencyStrategy).isPresent())?
                hystrixConcurrencyStrategy.getThreadPool(threadPoolKey, corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue)
                :super.getThreadPool(threadPoolKey, corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    /**
     * Several methods need to be overridden. Either call the existingConcurrencyStrategy method
     * implementation or call the base HystrixConcurrencyStrategy.
     * @param maxQueueSize
     * @return
     */
    @Override
    public BlockingQueue<Runnable> getBlockingQueue(int maxQueueSize) {
        return (Optional.ofNullable(hystrixConcurrencyStrategy).isPresent())?
                hystrixConcurrencyStrategy.getBlockingQueue(maxQueueSize)
                :super.getBlockingQueue(maxQueueSize);
    }

    @Override
    public <T> Callable<T> wrapCallable(Callable<T> callable) {
        return (Optional.ofNullable(hystrixConcurrencyStrategy).isPresent())?
                hystrixConcurrencyStrategy.wrapCallable(new DelegatingUserContextCallable<T>(
                        callable,
                        UserContextHolder.getContext() //Inject your Callable implementation that will set the UserContext.
                ))
                :super.wrapCallable(new DelegatingUserContextCallable<T>(
                        callable,
                        UserContextHolder.getContext()
                ));
    }

    @Override
    public <T> HystrixRequestVariable<T> getRequestVariable(HystrixRequestVariableLifecycle<T> rv) {
        return (Optional.ofNullable(hystrixConcurrencyStrategy).isPresent())?
                hystrixConcurrencyStrategy.getRequestVariable(rv)
                :super.getRequestVariable(rv);
    }
}
