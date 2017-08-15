package org.maxur.mserv.doc;

import org.junit.After;
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

    @After
    public void tearDown() throws Exception {
        final BaseService service = Locator.Companion.service(BaseService.class);
        if (service != null) {
            service.stop();
        }
        Locator.Companion.shutdown();
    }

    @Test
    // tag::launcher[]
    public void main() {
        Java.service()
            .name(":name") // <1>
            .packages("org.maxur.mserv.sample") // <2>
            .properties("hocon") // <3>
            .rest() // <4>
            .beforeStart(this::beforeStart) // <5>
            .afterStop(this::afterStop)
            .start(); // <6>
    }
    // end::launcher[]

    private void onError(Exception exception) {

    }

    private void afterStop(final BaseService service) {

    }

    private void beforeStart(final BaseService service) {
        final Locator locator = service.getLocator();
        final PropertiesSource config = locator.service(PropertiesSource.class);
        assertThat(config).isNotNull();
        assertThat(config.getFormat()).isEqualToIgnoringCase("Hocon");
    }


}
