package org.maxur.mserv.sample;


import lombok.extern.slf4j.Slf4j;
import org.maxur.mserv.Condition;
import org.maxur.mserv.MServ;
import org.maxur.mserv.Menu;
import org.maxur.mserv.events.CriticalErrorOcurredEvent;
import org.maxur.mserv.events.PropertiesLoadedEvent;
import org.maxur.mserv.events.ServiceObserver;
import org.maxur.mserv.events.ServiceStartedEvent;
import org.maxur.mserv.events.ServiceStopedEvent;
import org.maxur.mserv.properties.PropertyLoadException;
import org.maxur.mserv.sample.conf.UserConfig;

import static org.maxur.mserv.MServ.service;
import static org.maxur.mserv.properties.PropertiesFile.asYaml;
import static org.maxur.mserv.properties.PropertiesFile.yamlFile;

/**
 * Application Launcher.
 *
 * @author myunusov
 * @version 1.0
 * @since <pre>10.07.2016</pre>
 */
@Slf4j
public final class Launcher implements ServiceObserver {

    private static final String CONFIG_YAML = "./conf/config.yaml";

    private Launcher() {
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(final String[] args) {
        service()
            .observeWith(new Launcher())
            .loadPropertiesFrom(yamlFile(CONFIG_YAML)).to(UserConfig.class)
            .loadConfigFrom(SampleSysConfig.class)
            .execute(Menu.commandBy(args));
    }

    @SuppressWarnings("unused")
    public void on(final ServiceStartedEvent event) {
        log.info("start");
    }

    @SuppressWarnings("unused")
    public void on(final PropertiesLoadedEvent event) {
        log.debug("properties was loaded. \n" + asYaml(event.properties()));
    }

    @SuppressWarnings("unused")
    public void on(final ServiceStopedEvent event) {
        log.info("stop");
    }

    @SuppressWarnings("unused")
    public void on(CriticalErrorOcurredEvent event){
        final RuntimeException error = event.error();
        final MServ entity = event.entity();
        Condition.on(error).is(PropertyLoadException.class).exec(entity::terminate);
        log.error("error", error);
    }


}