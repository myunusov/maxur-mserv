package org.maxur.mserv.core.rest

import com.nhaarman.mockito_kotlin.verify
import org.assertj.core.api.Assertions.assertThat
import org.glassfish.hk2.utilities.binding.AbstractBinder
import org.junit.Test
import org.junit.runner.RunWith
import org.maxur.mserv.core.MicroService
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import java.io.IOException
import javax.ws.rs.client.Entity
import javax.ws.rs.core.MediaType


@RunWith(MockitoJUnitRunner::class)
class ServiceResourceIT : AbstractResourceIT() {

    @Mock
    private lateinit var service: MicroService

    override fun resourceClass(): Class<*> = ServiceResource::class.java

    override fun configurator(): Function1<AbstractBinder, Unit> = { binder: AbstractBinder ->
        binder.bind(service).to(MicroService::class.java)
    }

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

    @Test
    @Throws(IOException::class)
    fun testServiceResourceStop() {
        val baseTarget = target("/service/stop")
        val response = baseTarget.request()
                .accept(MediaType.APPLICATION_JSON)
                .put(Entity.json(""))
        assertThat(response.status).isEqualTo(204)
        verify(service).deferredStop()
    }


    @Test
    @Throws(IOException::class)
    fun testServiceResourceRestart() {
        val baseTarget = target("/service/restart")
        val response = baseTarget.request()
                .accept(MediaType.APPLICATION_JSON)
                .put(Entity.json(""))
        assertThat(response.status).isEqualTo(204)
        verify(service).deferredRestart()
    }
}
