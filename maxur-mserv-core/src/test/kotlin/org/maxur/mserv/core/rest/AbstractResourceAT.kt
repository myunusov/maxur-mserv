package org.maxur.mserv.core.rest

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider
import org.glassfish.hk2.utilities.binding.AbstractBinder
import org.glassfish.jersey.ServiceLocatorProvider
import org.glassfish.jersey.jackson.JacksonFeature
import org.glassfish.jersey.media.multipart.MultiPartFeature
import org.glassfish.jersey.server.ResourceConfig
import org.glassfish.jersey.server.ServerProperties
import org.glassfish.jersey.test.JerseyTest
import org.glassfish.jersey.test.TestProperties
import org.maxur.mserv.core.service.jackson.ObjectMapperProvider
import javax.ws.rs.core.Application
import javax.ws.rs.core.Feature
import javax.ws.rs.core.FeatureContext

/**
 * @author myunusov
 * @version 1.0
 * @since <pre>07.07.2017</pre>
 */
abstract class AbstractResourceAT : JerseyTest() {

    protected val mapper: ObjectMapper = ObjectMapperProvider().provide()

    override fun configure(): Application {
        enable(TestProperties.LOG_TRAFFIC)
        enable(TestProperties.DUMP_ENTITY)
        return object : ResourceConfig(resourceClass()) {
            init {
                property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true)
                property(ServerProperties.BV_DISABLE_VALIDATE_ON_EXECUTABLE_OVERRIDE_CHECK, true)
                val provider = JacksonJaxbJsonProvider()
                provider.setMapper(mapper)
                register(provider)
                register(JacksonFeature::class.java)
                register(RuntimeExceptionHandler::class.java)
                register(ServiceLocatorFeature())
                register(ServiceEventListener("/"))
                register(MultiPartFeature::class.java)
                register(object : AbstractBinder() {
                    override fun configure() {
                        configurator().invoke(this)
                    }
                })
            }
        }
    }

    protected abstract fun resourceClass(): Class<*>

    protected abstract fun configurator(): (AbstractBinder) -> Unit

    private class ServiceLocatorFeature : Feature {
        override fun configure(context: FeatureContext): Boolean {
            ServiceLocatorProvider.getServiceLocator(context)
            return true
        }

    }
}