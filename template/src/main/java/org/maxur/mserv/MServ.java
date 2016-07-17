package org.maxur.mserv;

import org.maxur.mserv.events.CriticalErrorOcurredEvent;
import org.maxur.mserv.events.ParametersLoadedEvent;
import org.maxur.mserv.events.ServiceObserver;
import org.maxur.mserv.events.ServiceStartedEvent;
import org.maxur.mserv.events.ServiceStopedEvent;
import org.maxur.mserv.properties.PropertiesFile;
import org.maxur.mserv.properties.PropertyLoadException;

import static java.util.Optional.empty;

/**
 * The type M serv.
 *
 * @author myunusov
 * @version 1.0
 * @since <pre>14.07.2016</pre>
 */
public abstract class MServ {

    private ServiceObserver observer = ServiceObserver.defaut();

    private Object properties;

    private boolean isTerminated = false;


    /**
     * Instantiates a new M serv.
     */
    protected MServ() {
    }

    /**
     * Create m serv.
     *
     * @return the m serv
     */
    public static MServ restService() {
        return new MRestServ();
    }

    /**
     * Read properties from properties class.
     *
     * @param propertiesClass the properties Class
     * @return the Child
     */
    public MServ loadPropertiesFrom(final Class<?> propertiesClass) {
        try {
            this.properties = propertiesClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            final PropertyLoadException exception =
                new PropertyLoadException(
                    e,
                    "Properties class '%s' is not accessible", propertiesClass.getSimpleName()
                );
            observer.apply(new CriticalErrorOcurredEvent(this, exception));
        }
        observer.apply(new ParametersLoadedEvent(this, properties));
        return this;
    }


    /**
     * Read properties from properties object.
     *
     * @param properties the properties
     * @return the Child
     */
    public MServ loadPropertiesFrom(final Object properties) {
        this.properties = properties;
        observer.apply(new ParametersLoadedEvent(this, properties));
        return this;
    }

    /**
     * Read properties from properties file.
     *
     * @param propertiesFile the properties file
     * @return the Child
     */
    public PropertiesFileBinder loadPropertiesFrom(final PropertiesFile propertiesFile) {
        return propertiesClass -> bindProperties(propertiesFile, propertiesClass);
    }

    private MServ bindProperties(final PropertiesFile propertiesFile, final Class<?> propertiesClass) {
        try {
            properties = propertiesFile.bindPropertiesClass(propertiesClass);
            observer.apply(new ParametersLoadedEvent(this, properties));
        } catch (RuntimeException e) {
            properties = empty();
            observer.apply(new CriticalErrorOcurredEvent(this, e));
        }
        return MServ.this;
    }

    /**
     * Load config from m serv.
     *
     * @param configClass the config class
     * @return the m serv
     */
    public MServ loadConfigFrom(Class<? extends SysConfig> configClass) {
        return this;
    }

    /**
     * Run.
     */
    public void run() {
        try {
            if (isTerminated) {
                return;
            }
            observer.apply(new ServiceStartedEvent(this));
            observer.apply(new ServiceStopedEvent(this));
        } catch (RuntimeException e) {
            observer.apply(new CriticalErrorOcurredEvent(this, e));
        }
    }


    /**
     * Add observer m serv.
     *
     * @param observer the observer
     * @return the m serv
     */
    public MServ addObserver(final ServiceObserver observer) {
        this.observer = observer;
        return this;
    }

    /**
     * Terminate.
     */
    public void terminate() {
        isTerminated = true;
    }


    /**
     * The interface Properties file binder.
     * <p>
     * The interface Properties file binder.
     */
    @FunctionalInterface
    public interface PropertiesFileBinder {
        /**
         * To m serv.
         *
         * @param propertiesClass the properties class
         * @return the m serv
         */
        MServ to(Class<?> propertiesClass);
    }

}
