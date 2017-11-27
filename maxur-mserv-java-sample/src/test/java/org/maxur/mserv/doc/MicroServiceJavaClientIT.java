package org.maxur.mserv.doc;

import org.junit.Test;
import org.maxur.mserv.frame.domain.BaseService;
import org.maxur.mserv.frame.java.Locator;
import org.maxur.mserv.frame.runner.Java;
import org.maxur.mserv.frame.service.properties.Properties;

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
        Java.runner()
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
        Locator.stop();
    }

    private void beforeStop(final BaseService service) {
        assertThat(service).isNotNull();
    }

    private void afterStart(final BaseService service) {
        service1 = service;
        assertThat(service).isNotNull();
        final Locator locator = Locator.getInstance();
        final Properties config = locator.service(Properties.class);
        assertThat(config).isNotNull();
        assertThat(config.getSources().get(0).getFormat()).isEqualToIgnoringCase("Hocon");
    }


}
