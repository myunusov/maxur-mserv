package org.maxur.mserv.sample.rest;

import io.swagger.annotations.Contact;
import io.swagger.annotations.Info;
import io.swagger.annotations.License;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import org.jvnet.hk2.annotations.Service;
import org.maxur.mserv.frame.annotation.Value;
import org.maxur.mserv.frame.rest.RestResourceConfig;

import javax.inject.Inject;

/**
 * @author myunusov
 * @version 1.0
 * @since <pre>12.06.2017</pre>
 */
@SuppressWarnings("unused")
@SwaggerDefinition(
        info = @Info(
                title = "MY REST API",
                description = "This is a Maxur Microservice REST API",
                version = "V1.0",

            //    termsOfService = "http://theweatherapi.io/terms.html",
                contact = @Contact(
                        name = "Maxim Yunusov",
                        email = "myunusov@maxur.org",
                        url = "https://github.com/myunusov"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "http://www.apache.org/licenses/LICENSE-2.0"
                )
        ),
        consumes = {"application/json", "application/xml"},
        produces = {"application/json", "application/xml"},
        schemes = {SwaggerDefinition.Scheme.HTTP, SwaggerDefinition.Scheme.HTTPS},
        tags = {
            @Tag(name = "Private", description = "Tag used to denote operations as private")
        }
       // externalDocs = ExternalDocs(value = "Meteorology", url = "http://theweatherapi.io/meteorology.html")
)
@Service
public class RestServiceConfig extends RestResourceConfig {

    @Inject
    public RestServiceConfig(@Value(key = "name") String name) {
        setApplicationName(name);
        resources(getClass().getPackage().getName());
    }
    
}
