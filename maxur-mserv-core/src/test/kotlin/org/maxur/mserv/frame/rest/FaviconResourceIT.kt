package org.maxur.mserv.frame.rest

import org.assertj.core.api.Assertions
import org.glassfish.hk2.utilities.binding.AbstractBinder
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.io.IOException
import kotlin.reflect.KClass

@RunWith(MockitoJUnitRunner::class)
class FaviconResourceIT : AbstractResourceAT() {

    override fun configurator() = { _: AbstractBinder -> }

    override fun resourceClass(): KClass<out Any> = FaviconResource::class

    @Test
    @Throws(IOException::class)
    fun testFaviconGet() {
        val baseTarget = target("/favicon.ico")
        val array = baseTarget.request()
                .accept("image/x-icon")
                .get(ByteArray::class.java)
        Assertions.assertThat(array).isNotEmpty()
    }

    @Test
    @Throws(IOException::class)
    fun testInvalidFaviconGet() {
        val baseTarget = target("/invalid.ico")
        val response = baseTarget.request()
                .accept("image/x-icon")
                .get()
        Assertions.assertThat(response.status).isEqualTo(404)
    }
}