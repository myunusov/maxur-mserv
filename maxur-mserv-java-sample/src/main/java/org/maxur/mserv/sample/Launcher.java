package org.maxur.mserv.sample;

import org.maxur.mserv.core.MicroService;
import org.maxur.mserv.core.domain.Service;
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
/*
        DSL.service {
            title = ":name"
            packages = "org.maxur.mserv.sample"
            observers {
                beforeStart = this @Launcher::beforeStart
                    afterStop = this @Launcher::afterStop
                    onError = this @Launcher::onError
            }
            properties {
                format = "hocon"
            }
            services {
                rest {
                }
            }
        }.start()
*/
        Java.dsl.service()
            .title(":name")
            .packages("org.maxur.mserv.sample")
            .properties("hocon")
            .service("grizzly", ":webapp")
            .build()
            .start();
    }

    public void beforeStart(MicroService service, ConfigParams configParams) {
        configParams.log();
        log.info("${service.name} is started");
    }

    public void afterStop(Service service) {
        log.info("${service.name} is stopped");
    }

    public void onError(Exception exception) {
        log.error(exception.getMessage(), exception);
    }

}

