package org.maxur.mserv.reflection;

import eu.infomas.annotation.AnnotationDetector;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * The type Class repository.
 *
 * @author myunusov
 * @version 1.0
 * @since <pre>29.07.2016</pre>
 */
@Slf4j
public class ClassRepository {

    private final String[] packageNames;

    private final Map<Class<? extends Annotation>, Consumer<Class<?>>> rules = new HashMap<>();

    private ClassRepository(final String[] packageNames) {
        this.packageNames = packageNames;
    }

    /**
     * By packages class repository.
     *
     * @param packageNames the package names
     * @return the class repository
     */
    public static ClassRepository byPackages(final String[] packageNames) {
        return new ClassRepository(packageNames);
    }

    /**
     * By classpath class repository.
     *
     * @return the class repository
     */
    public static ClassRepository byClasspath() {
        return new ClassRepository(new String[]{});
    }

    /**
     * Add rule.
     *
     * @param annotation the annotation
     * @param action     the action
     */
    public void addRule(final Class<? extends Annotation> annotation, final Consumer<Class<?>> action) {
        rules.put(annotation, action);
    }

    /**
     * Scan package.
     */
    public void scanPackage() {

        final AnnotationDetector.TypeReporter reporter = new AnnotationDetector.TypeReporter() {

            @Override
            public void reportTypeAnnotation(final Class<? extends Annotation> aClass, final String className) {
                rules.getOrDefault(aClass, c -> {
                    //NOP
                }).accept(ClassUtils.getClassByName(className));
            }

            @SuppressWarnings("unchecked")
            @Override
            public Class<? extends Annotation>[] annotations() {
                return rules.keySet().toArray(new Class[rules.size()]);
            }

        };

        final AnnotationDetector cf = new AnnotationDetector(reporter);
        try {
            if (packageNames.length == 0) {
                cf.detect();
            } else {
                cf.detect(packageNames);
            }
        } catch (IOException e) {
            log.error("Error on detect annotated classes: ", e);
            throw new IllegalStateException(e.getMessage(), e);
        }

    }


}
