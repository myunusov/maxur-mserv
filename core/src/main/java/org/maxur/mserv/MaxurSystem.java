package org.maxur.mserv;

import lombok.extern.slf4j.Slf4j;
import org.maxur.mserv.bus.Bus;
import org.maxur.mserv.config.ConfigFile;
import org.maxur.mserv.core.annotation.Binder;
import org.maxur.mserv.core.annotation.Configuration;
import org.maxur.mserv.core.annotation.Observer;
import org.maxur.mserv.ioc.ServiceLocator;
import org.maxur.mserv.ioc.hk2.Hk2System;
import org.maxur.mserv.microservice.MicroService;
import org.maxur.mserv.reflection.ClassRepository;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Strings.isNullOrEmpty;
import static java.lang.String.format;
import static org.maxur.mserv.reflection.ClassRepository.byPackages;

/**
 * The type Maxur system.
 *
 * @author myunusov
 * @version 1.0
 * @since <pre>01.08.2016</pre>
 */
@Slf4j
public class MaxurSystem {

    private final String name;

    private final List<Class<?>> binders = new ArrayList<>();

    private final List<Class<?>> observers = new ArrayList<>();

    private final List<Class<?>> configurations = new ArrayList<>();

    private final Hk2System diSystem = new Hk2System();

    private MaxurSystem(final String name) {
        this.name = name;
    }

    /**
     * System maxur system.
     *
     * @param name the name
     * @return the maxur system
     */
    public static MaxurSystem system(final String name) {
        return new MaxurSystem(name);
    }

    /**
     * With aop in packages maxur system.
     *
     * @param packageNames the package names
     * @return the maxur system
     */
    public MaxurSystem withAopInPackages(final String... packageNames) {
        final ClassRepository classRepository = byPackages(packageNames);
        classRepository.addRule(Observer.class, this::collectObserverBy);
        classRepository.addRule(Binder.class, this::collectBinderBy);
        classRepository.addRule(Configuration.class, this::collectConfigurationBy);
        classRepository.scanPackage();
        return this;
    }

    private void collectBinderBy(final Class<?> aClass) {
        binders.add(aClass);
    }

    private void collectConfigurationBy(final Class<?> aClass) {
        configurations.add(aClass);
    }

    private void collectObserverBy(final Class<?> aClass) {
        observers.add(aClass);
    }

    /**
     * Start.
     *
     * @param serviceName the service name
     */
    public void start(final String serviceName) {

        diSystem.init(binders);

        config();
        addObservers();
        locator().bean(MicroService.class, serviceName)
                .withName(name)
                .start();
    }

    private void addObservers() {
        observers.forEach(this::makeObserverBy);
    }

    private void config() {
        switch (configurations.size()) {
            case 0:
                log.warn("Configuration class (with 'Configuration' annotation) is not found");
                break;
            case 1:
                makeConfigurationBy(configurations.get(0));
                break;
            default:
                log.error("More than one configuration class (with 'Configuration' annotation) is found");
        }
    }

    private void makeConfigurationBy(final Class<?> clazz) {
        final Configuration clazzAnnotation = clazz.getAnnotation(Configuration.class);
        final String fileName = clazzAnnotation.fileName();
        final Object config = isNullOrEmpty(fileName) ?
                diSystem.instanceOf(clazz) :
                loadConfig(fileName, clazz);
        diSystem.configResolver().setConfig(config);
    }

    private Object loadConfig(final String fileName, final Class<?> clazz) {
        final ConfigFile configFile = ConfigFile.yamlFile(fileName);
        try {
            final Object result = configFile.bindTo(clazz);
            log.debug(format("Config file '%s' is loaded. %n %s", fileName, ConfigFile.asYaml(result)));
            return result;
        } catch (RuntimeException e) {
            log.error(format("Config file '%s' is not loaded", fileName));
            log.debug(format("Config file '%s' is not loaded", fileName), e);
            throw e;
        }
    }

    private void makeObserverBy(final Class<?> clazz) {
        locator().bean(Bus.class, "event.bus").register(diSystem.instanceOf(clazz));
    }

    private ServiceLocator locator() {
        return diSystem.locator();
    }
}
