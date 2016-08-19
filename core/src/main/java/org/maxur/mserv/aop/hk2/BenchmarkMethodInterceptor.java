/*
 * Copyright 2015 Russian Post
 *
 * This source code is Russian Post Confidential Proprietary.
 * This software is protected by copyright. All rights and titles are reserved.
 * You shall not use, copy, distribute, modify, decompile, disassemble or reverse engineer the software.
 * Otherwise this violation would be treated by law and would be subject to legal prosecution.
 * Legal use of the software provides receipt of a license from the right holder only.
 */


package org.maxur.mserv.aop.hk2;

import com.ecyrd.speed4j.StopWatch;
import com.ecyrd.speed4j.StopWatchFactory;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;

/**
 * This is an interceptor that will implement benchmark of method latency.
 * <p>
 * This interceptor does not import any hk2 API and thus
 * is a pure AOP Alliance method interceptor that might be
 * used in any software that enabled AOP Alliance interceptors
 *
 * @author myunusov
 * @version 1.0
 * @since <pre>02.09.2015</pre>
 */
class BenchmarkMethodInterceptor implements MethodInterceptor {

    private final StopWatchFactory stopWatchFactory = StopWatchFactory.getInstance("loggingFactory");

    /*
     * (non-Javadoc)
     * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
     */
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        final Method method = invocation.getMethod();
        final String className = method.getDeclaringClass().getSimpleName();
        final String methodName = method.getName();
        final StopWatch stopWatch = stopWatchFactory.getStopWatch();
        try {
            Object result = invocation.proceed();
            stopWatch.stop(className + " " + methodName + " : success");
            return result;
        } catch (Exception e) {
            stopWatch.stop(className + " " + methodName + " : error");
            throw e;
        }
    }

}
