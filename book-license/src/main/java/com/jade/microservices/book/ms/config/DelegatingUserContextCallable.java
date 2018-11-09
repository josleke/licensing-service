package com.jade.microservices.book.ms.config;

import com.jade.microservices.book.ms.util.UserContext;
import com.jade.microservices.book.ms.util.UserContextHolder;

import java.util.concurrent.Callable;

/**
 * @Author: Josiah Adetayo
 * @Email: j.adetayo@bcs.org.uk, josiah.adetayo@cwg-plc.com
 * @Date: 5/11/18
 */
public final class DelegatingUserContextCallable<V> implements Callable<V> {

    private final Callable<V> delegate;
    private UserContext originalUserContext;

    public DelegatingUserContextCallable(Callable<V> delegate, UserContext originalUserContext) {
        this.delegate = delegate;
        this.originalUserContext = originalUserContext;
    }

    /**
     * The call() function is invoked before the method protected by the @HystrixCommand annotation
     * @return
     * @throws Exception
     */
    @Override
    public V call() throws Exception {

        // The UserContext is set. The ThreadLocal variable that stores the UserContext is associated
        // with the thread running the Hystrix protected method.
        UserContextHolder.setContext(originalUserContext);

        try{ //Once the UserContext is set invoke the call() method on the Hystrix protected method
            return delegate.call();
        }finally {
            originalUserContext = null;
        }

    }

    public static <V> Callable<V> create(Callable<V> delegate, UserContext userContext) {
        return new DelegatingUserContextCallable<V>(delegate, userContext);
    }
}
