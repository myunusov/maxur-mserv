/*
 * Copyright 2015 Russian Post
 *
 * This source code is Russian Post Confidential Proprietary.
 * This software is protected by copyright. All rights and titles are reserved.
 * You shall not use, copy, distribute, modify, decompile, disassemble or reverse engineer the software.
 * Otherwise this violation would be treated by law and would be subject to legal prosecution.
 * Legal use of the software provides receipt of a license from the right holder only.
 */

package org.maxur.mserv.config.hk2;

import lombok.extern.slf4j.Slf4j;
import org.glassfish.hk2.api.Injectee;
import org.glassfish.hk2.api.InjectionResolver;
import org.glassfish.hk2.api.ServiceHandle;
import org.maxur.mserv.config.Config;
import org.maxur.mserv.config.ConfigResolver;
import org.maxur.mserv.core.annotation.Param;

import java.util.Objects;

import static java.lang.String.format;

/**
 * The type Configuration injection resolver.
 *
 * @author Maxim Yunusov
 * @version 1.0
 * @since <pre>9/2/2015</pre>
 */
@Slf4j
public class ConfigurationInjectionResolver implements ConfigResolver, InjectionResolver<Param> {

    private Config config;

    @Override
    public Object resolve(final Injectee injectee, final ServiceHandle<?> root) {
        final Param annotation = injectee.getParent().getAnnotation(Param.class);
        final String key = annotation.key();
        final boolean optional = annotation.optional();
        final Object result = config.valueBy(key);
        if (Objects.isNull(result) && !optional) {
            log.error(format("Property '%s' is not found", key));
        }
        return result;
    }

    @Override
    public boolean isConstructorParameterIndicator() {
        return false;
    }

    @Override
    public boolean isMethodParameterIndicator() {
        return false;
    }

    /**
     * Sets config.
     *
     * @param config the config
     */
    @Override
    public void setConfig(final Config config) {
        this.config = config;
    }


}
