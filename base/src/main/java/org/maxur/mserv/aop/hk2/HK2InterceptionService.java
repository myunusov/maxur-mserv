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

import org.aopalliance.intercept.ConstructorInterceptor;
import org.aopalliance.intercept.MethodInterceptor;
import org.glassfish.hk2.api.Filter;
import org.glassfish.hk2.api.InterceptionService;
import org.glassfish.hk2.utilities.BuilderHelper;
import org.maxur.mserv.aop.Benchmark;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.emptyList;

/**
 * The type Hk 2 interception service.
 *
 * @author myunusov
 * @version 1.0
 * @since <pre>02.09.2015</pre>
 */
public class HK2InterceptionService implements InterceptionService {

    private static final MethodInterceptor METHOD_INTERCEPTOR = new BenchmarkMethodInterceptor();

    private static final List<MethodInterceptor> METHOD_LIST = Collections.singletonList(METHOD_INTERCEPTOR);

    @Override
    public Filter getDescriptorFilter() {
        return BuilderHelper.allFilter();
    }

    @Override
    public List<ConstructorInterceptor> getConstructorInterceptors(Constructor<?> constructor) {
        return emptyList();
    }

    @Override
    public List<MethodInterceptor> getMethodInterceptors(Method method) {
        if (method.isAnnotationPresent(Benchmark.class)) {
            return METHOD_LIST;
        }
        return emptyList();
    }

}

