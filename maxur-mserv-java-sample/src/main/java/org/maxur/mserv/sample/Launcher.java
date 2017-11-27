package org.maxur.mserv.sample;

import org.maxur.mserv.frame.domain.BaseService;
import org.maxur.mserv.frame.java.Locator;
import org.maxur.mserv.frame.runner.Java;
import org.maxur.mserv.frame.service.properties.Properties;
import org.maxur.mserv.sample.params.ConfigParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Application Launcher
 *
 * @author myunusov
 * @version 1.0
 * @since <pre>12.06.2017</pre>
 */
public final class Launcher {

    private static final  Logger log = LoggerFactory.getLogger(Launcher.class);

    /**
     * Command line entry point. This method kicks off the building of a application  object
     * and executes it.
     *
     * @param args - arguments of command.
     */
    public static void main(String[] args) {
        Java.runner()
            .name(":name")
            .packages("org.maxur.mserv.sample")
            .properties("hocon")
            .rest()
            .afterStart(Launcher::afterStart)
            .beforeStop(service -> log.info("Service is stopped"))
            .onError(exception -> log.error(exception.getMessage(), exception) )
            .start();
    }
    
    private static void afterStart(final BaseService service) {
        final Locator locator = Locator.getInstance();
        final Properties config = locator.service(Properties.class);
        if (config != null) {
            log.info("Properties Source is '{}'", config.getSources().get(0).getFormat());
            final ConfigParams configParams = locator.service(ConfigParams.class);
            if (configParams != null) {
                configParams.log();
            }
        }
        log.info("{} is started", service.getName());
    }

}

