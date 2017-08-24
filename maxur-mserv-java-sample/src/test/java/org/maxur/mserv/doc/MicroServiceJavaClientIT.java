package org.maxur.mserv.doc;

import org.junit.Test;
import org.maxur.mserv.core.Locator;
import org.maxur.mserv.core.domain.BaseService;
import org.maxur.mserv.core.service.msbuilder.Java;
import org.maxur.mserv.core.service.properties.PropertiesSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Maxim Yunusov
 * @version 1.0
 * @since <pre>8/4/2017</pre>
 */
public class MicroServiceJavaClientIT {

    private BaseService service1 = null;

    @Test
    public void main() {
        // tag::launcher[]
        Java.service()
            .name(":name") // <1>
            .packages("org.maxur.mserv.sample") // <2>
            .properties("hocon") // <3>
            .rest() // <4>
            .afterStart(this::afterStart) // <5>
            .beforeStop(this::beforeStop)
            .start(); // <6>
        // end::launcher[]
        if (service1 != null) {
            service1.stop();
        }
        Locator.Companion.shutdown();
    }

    private void beforeStop(final BaseService service) {
        assertThat(service).isNotNull();
    }

    private void afterStart(final BaseService service) {
        service1 = service;
        assertThat(service).isNotNull();
        final Locator locator = service.getLocator();
        final PropertiesSource config = locator.service(PropertiesSource.class);
        assertThat(config).isNotNull();
        assertThat(config.getFormat()).isEqualToIgnoringCase("Hocon");
    }


}
