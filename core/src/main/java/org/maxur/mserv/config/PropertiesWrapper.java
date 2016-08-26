package org.maxur.mserv.config;

import lombok.experimental.Delegate;
import lombok.extern.slf4j.Slf4j;
import org.maxur.mserv.core.annotation.Key;
import org.maxur.mserv.reflection.ClassUtils;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;

/**
 * The type Config.
 *
 * @author Maxim Yunusov
 * @version 1.0
 * @since <pre>8/19/2016</pre>
 */
@Slf4j
public class PropertiesWrapper {

    @Delegate
    private final Object properties;

    private final Map<String, Object> map = new HashMap<>();

    private PropertiesWrapper(final Object properties) {
        this.properties = properties;
        parse(properties);
    }

    /**
     * Wrap properties wrapper.
     *
     * @param properties the properties
     * @return the properties wrapper
     */
    public static PropertiesWrapper wrap(Object properties) {
        return new PropertiesWrapper(properties);
    }

    /**
     * Returns value by key.
     *
     * @param <T> the type of value
     * @param key the key
     * @return the value by key
     */

    @SuppressWarnings("unchecked")
    public <T> T valueBy(String key) {
        return (T) map.get(key);
    }

    /**
     * Gets object.
     *
     */
    private void parse(Object config) {
        for (Field field : config.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                log.debug(format("%s - %s - %s", field.getName(), field.getType(), field.get(config)));
                if (isComposedObject(field)) {
                    if (field.isAnnotationPresent(Key.class)) {
                        map.put(field.getAnnotation(Key.class).value(), field.get(config));
                        continue;
                    } else {
                        parse(field.get(config));
                    }
                }
                map.put(field.getName(), field.get(config));
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
