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
import org.maxur.mserv.config.ConfigResolver;
import org.maxur.mserv.core.annotation.Key;
import org.maxur.mserv.core.annotation.Param;
import org.maxur.mserv.reflection.ClassUtils;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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

    private Object config;

    private final Map<String, Object> keys = new HashMap<>();

    @Override
    public Object resolve(final Injectee injectee, final ServiceHandle<?> root) {
        Param annotation = injectee.getParent().getAnnotation(Param.class);
        final String name = annotation.key();
        final boolean optional = annotation.optional();
        final Object result = keys.get(name);
        if (Objects.isNull(result) && !optional) {
            log.error(format("Property '%s' is not found", name));
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
    public void setConfig(Object config) {
        this.config = config;
        parse(config);
    }


    /**
     * Gets object.
     *
     * @param config the config
     */
    private void parse(Object config) {
        for (Field field : config.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                log.debug(format("%s - %s - %s", field.getName(), field.getType(), field.get(config)));
                if (isComposedObject(field)) {
                    if (field.isAnnotationPresent(Key.class)) {
                        keys.put(field.getAnnotation(Key.class).value(), field.get(config));
                        continue;
                    } else {
                        parse(field.get(config));
                    }
                }
                keys.put(field.getName(), field.get(config));
            } catch (IllegalAccessException e) {
                log.error(e.getMessage(), e);
                throw new IllegalStateException(e);
            }
        }
    }

    private static boolean isComposedObject(Field field) {
        return !field.getType().equals(Date.class) && !ClassUtils.isPrimitiveOrWrapper(field.getType());
    }
}
