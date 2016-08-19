package org.maxur.mserv.reflection;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static java.lang.String.format;

/**
 * The type Reflection utils.
 *
 * @author myunusov
 * @version 1.0
 * @since <pre>30.07.2016</pre>
 */
@Slf4j
public final class ClassUtils {

    private ClassUtils() {
    }

    /**
     * Create class instance object.
     *
     * @param clazz the clazz
     * @return the object
     */
    @NotNull
    public static Object createClassInstance(final Class<?> clazz) {
        try {
            final Constructor<?> ctor = clazz.getConstructor();
            return ctor.newInstance();
        } catch (InstantiationException | InvocationTargetException e) {
            log.error("Error on create observer: ", e);
            throw new IllegalStateException(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            log.error(format("Error on create observer (Illegal Access to Class %s): ", clazz.getSimpleName()), e);
            throw new IllegalStateException(e.getMessage(), e);
        } catch (NoSuchMethodException e) {
            log.error(format("Error on create observer (Constructor of Class %s not found) : ",
                clazz.getSimpleName()), e);
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    /**
     * Gets class by name.
     *
     * @param className the class name
     * @return the class by name
     */
    public static Class<?> getClassByName(final String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            log.error(format("Error on create observer (Class %s not found): ", className), e);
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    /**
     * Is primitive or wrapper boolean.
     *
     * @param aClass the a class
     * @return the boolean
     */
    public static boolean isPrimitiveOrWrapper(final Class<?> aClass) {
        return aClass.equals(Integer.class) ||
                aClass.equals(Long.class) ||
                aClass.equals(Boolean.class) ||
                aClass.equals(String.class) ||
                aClass.isPrimitive();
    }
}
