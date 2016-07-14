package org.maxur.mserv.sample;


import lombok.extern.slf4j.Slf4j;
import org.maxur.mserv.MServ;
import org.maxur.mserv.Result;
import org.maxur.mserv.sample.conf.UserConfig;

import static org.maxur.mserv.properties.PropertiesFile.yamlFile;

/**
 * Application Launcher.
 *
 * @author myunusov
 * @version 1.0
 * @since <pre>10.07.2016</pre>
 */
@Slf4j
public final class Launcher {

    private static final String CONFIG_YAML = "./conf/config.yaml";

    private Launcher() {
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        MServ.create()
            .loadPropertiesFrom(yamlFile(CONFIG_YAML)).to(UserConfig.class)
            .loadConfigFrom(SampleSysConfig.class)
            .hookOnStart(Launcher::onStart)
            .hookOnStop(Launcher::onStop)
            .hookOnError(Launcher::onError)
            .run();
    }

    private static Result onStart(final MServ serv) {
        log.info("start");
        return null;
    }

    private static Result onStop(final MServ serv) {
        log.info("stop");
        return null;
    }

    private static Result onError(final MServ serv, final RuntimeException e) {
        log.error("error", e);
        return null;
    }



}