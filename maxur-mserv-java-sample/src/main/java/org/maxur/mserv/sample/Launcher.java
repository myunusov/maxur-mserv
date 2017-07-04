package org.maxur.mserv.sample;

import org.maxur.mserv.core.Locator;
import org.maxur.mserv.core.MicroService;
import org.maxur.mserv.core.service.msbuilder.Java;
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

    private final static Logger log = LoggerFactory.getLogger(Launcher.class);

    /**
     * Command line entry point. This method kicks off the building of a application  object
     * and executes it.
     *
     * @param args - arguments of command.
     */
    public static void main(String[] args) {

        Java.dsl.service()
            .title(":name")
            .packages("org.maxur.mserv.sample")
            .properties("hocon")
            .rest()
            .beforeStart(Launcher::beforeStart)
            .afterStop( () -> log.info("Service is stopped"))
            .onError((exception) -> log.error(exception.getMessage(), exception) )
            .start();
    }
    
    private static void beforeStart() {
        final Locator locator = Locator.Companion.getCurrent();
        final MicroService service = locator.service(MicroService.class);
        final ConfigParams configParams = locator.service(ConfigParams.class);
        if (configParams != null) {
            configParams.log();
        }
        if (service != null) {
            log.info("{} is started", service.getName());
        }
    }

}

