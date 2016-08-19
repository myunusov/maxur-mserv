package org.maxur.mserv;

import lombok.extern.slf4j.Slf4j;
import org.maxur.mserv.bus.Bus;
import org.maxur.mserv.config.PropertiesWrapper;
import org.maxur.mserv.config.ConfigFile;
import org.maxur.mserv.config.ConfigFileFactory;
import org.maxur.mserv.core.annotation.Configuration;
import org.maxur.mserv.core.annotation.Properties;
import org.maxur.mserv.core.annotation.Observer;
import org.maxur.mserv.ioc.Framework;
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

    private final List<Class<?>> configurations = new ArrayList<>();

    private final List<Class<?>> observers = new ArrayList<>();

    private final List<Class<?>> properties = new ArrayList<>();

    private final Framework framework;

    private MaxurSystem(final String name, final Framework framework) {
        this.name = name;
        this.framework = framework;
    }

    /**
     * System maxur system.
     *
     * @param name the name
     * @param framework Service framework
     * @return the maxur system
     */
    public static MaxurSystem system(final String name, final Framework framework) {
        return new MaxurSystem(name, framework);
    }

    /**
     * With aop in packages maxur system.
     *
     * @param packageNames the package names
     * @return the maxur system
     */
    public MaxurSystem scanForConfig(final String... packageNames) {
        final ClassRepository classRepository = byPackages(packageNames);
        classRepository.addRule(Observer.class, this::collectObserverBy);
        classRepository.addRule(Configuration.class, this::collectBinderBy);
        classRepository.addRule(Properties.class, this::collectPropertiesBy);
        classRepository.scanPackage();
        return this;
    }

    private void collectBinderBy(final Class<?> aClass) {
        configurations.add(aClass);
    }

    private void collectPropertiesBy(final Class<?> aClass) {
        properties.add(aClass);
    }

    private void collectObserverBy(final Class<?> aClass) {
        observers.add(aClass);
    }

    /**
     * Start.
     *
     * @param beanName the service name
     */
    public void startAs(final String beanName) {
        init();
        start(beanName);
    }

    private void init() {
        framework.configWith(configurations);
        setup();
        addObservers();
    }

    private void start(final String serviceName) {
        framework.locator().bean(MicroService.class, serviceName)
                .withName(name)
                .start();
    }

    private void addObservers() {
        observers.forEach(this::registerObserverBy);
    }

    private void registerObserverBy(final Class<?> clazz) {
        framework.locator().bean(Bus.class, "event.bus").register(framework.instanceOf(clazz));
    }

    private void setup() {
        switch (properties.size()) {
            case 0:
                log.warn("Configuration class (with 'Configuration' annotation) is not found");
                break;
            case 1:
                makeConfigurationBy(properties.get(0));
                break;
            default:
                log.error("More than one configuration class (with 'Configuration' annotation) is found");
        }
    }

    private void makeConfigurationBy(final Class<?> clazz) {
        final Properties clazzAnnotation = clazz.getAnnotation(Properties.class);
        final String fileName = clazzAnnotation.fileName();
        final Object object = isNullOrEmpty(fileName) ?
                framework.instanceOf(clazz) :
                loadConfig(fileName, clazz);
        framework.configResolver().setProperties(PropertiesWrapper.wrap(object));
    }

    private Object loadConfig(final String fileName, final Class<?> clazz) {
        final ConfigFile configFile = framework.locator().bean(ConfigFileFactory.class).make(fileName);
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


}
