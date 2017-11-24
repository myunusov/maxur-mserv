package org.maxur.mserv.frame.rest

import org.assertj.core.api.Assertions.assertThat
import org.glassfish.hk2.utilities.binding.AbstractBinder
import org.junit.Test
import org.junit.runner.RunWith
import org.maxur.mserv.frame.MicroService
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import java.io.IOException
import kotlin.reflect.KClass

@RunWith(MockitoJUnitRunner::class)
class ServiceResourceIT : AbstractResourceAT() {

    @Mock
    private lateinit var service: MicroService

    override fun resourceClass(): KClass<out Any> = ServiceResource::class

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
        assertThat(node.findPath("command").findPath("href").asText())
            .isEqualTo("service/command")
    }
}
