package org.maxur.mserv.sample.params;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.jvnet.hk2.annotations.Service;
import org.maxur.mserv.core.annotation.Value;
import org.maxur.mserv.core.embedded.properties.WebAppProperties;
import org.maxur.mserv.core.service.jackson.ObjectMapperProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

/**
 * All Configuration Parameters
 *
 * @author myunusov
 * @version 1.0
 * @since <pre>12.06.2017</pre>
 */
@SuppressWarnings("unused")
@Service
public class ConfigParams {

    private final static Logger log = LoggerFactory.getLogger(ConfigParams.class);

    private final WebAppProperties webapp;

    private final String name;

    @Inject
    ConfigParams(
        @Value(key = "webapp") WebAppProperties webapp,
        @Value(key = "name")   String name
    ) {
        this.webapp = webapp;
        this.name = name;
    }

    public void log() {
        log.info("\n--- Configuration Parameters ---\n");
        log.info(this.toString());
        log.info("\n---------------------------------\n");
    }

    @Override
    public String toString() {
        return asText(this);
    }

    private String asText(final Object value) {
        final ObjectMapper mapper = ObjectMapperProvider.getObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            return mapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }

    public String getName() {
        return name;
    }

    public WebAppProperties getWebapp() {
        return webapp;
    }
}