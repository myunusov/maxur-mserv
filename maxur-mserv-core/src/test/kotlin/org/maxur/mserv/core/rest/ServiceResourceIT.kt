package org.maxur.mserv.core.rest

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider
import org.assertj.core.api.Assertions.assertThat
import org.glassfish.hk2.utilities.binding.AbstractBinder
import org.glassfish.jersey.ServiceLocatorProvider
import org.glassfish.jersey.jackson.JacksonFeature
import org.glassfish.jersey.media.multipart.MultiPartFeature
import org.glassfish.jersey.server.ResourceConfig
import org.glassfish.jersey.server.ServerProperties
import org.glassfish.jersey.test.JerseyTest
import org.glassfish.jersey.test.TestProperties
import org.junit.Test
import org.junit.runner.RunWith
import org.maxur.mserv.core.MicroService
import org.maxur.mserv.core.service.jackson.ObjectMapperProvider
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import java.io.IOException
import javax.ws.rs.core.Application
import javax.ws.rs.core.Feature
import javax.ws.rs.core.FeatureContext


@RunWith(MockitoJUnitRunner::class)
class ServiceResourceIT : JerseyTest() {

    private val mapper: ObjectMapper = ObjectMapperProvider().provide()

    @Mock
    private lateinit var service: MicroService

    @Test
    @Throws(IOException::class)
    fun testServiceResourceSelf() {
        `when`(service.version).thenReturn("0.1")
        `when`(service.name).thenReturn("test")

        val baseTarget = target("/service")
        val json = baseTarget.request()
                .accept("application/hal+json")
                .get(String::class.java)
        val node = mapper.readTree(json)

        assertThat(node.findPath("name").asText())
                .isEqualTo("test: 0.1")
        assertThat(node.findPath("self").findPath("href").asText())
                .isEqualTo("service")
        assertThat(node.findPath("stop").findPath("href").asText())
                .isEqualTo("service/stop")
        assertThat(node.findPath("restart").findPath("href").asText())
                .isEqualTo("service/restart")
    }


    override fun configure(): Application {
        enable(TestProperties.LOG_TRAFFIC)
        enable(TestProperties.DUMP_ENTITY)
        return object : ResourceConfig(ServiceResource::class.java) {
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

    private class ServiceLocatorFeature : Feature {
        override fun configure(context: FeatureContext): Boolean {
            ServiceLocatorProvider.getServiceLocator(context)
            return true
        }
    }

    protected fun configurator(): Function1<AbstractBinder, Unit> = { binder: AbstractBinder ->
        binder.bind(service).to(MicroService::class.java)
    }


}
