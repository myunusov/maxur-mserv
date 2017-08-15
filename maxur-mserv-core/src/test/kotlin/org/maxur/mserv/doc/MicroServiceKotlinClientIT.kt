package org.maxur.mserv.doc

import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.maxur.mserv.core.Locator
import org.maxur.mserv.core.domain.BaseService
import org.maxur.mserv.core.embedded.WebServer
import org.maxur.mserv.core.service.msbuilder.Kotlin
import org.maxur.mserv.core.service.properties.PropertiesSource
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MicroServiceKotlinClientIT {

    @After
    @Throws(Exception::class)
    fun tearDown() {
        val service = Locator.service(BaseService::class.java)
        service?.stop()
        Locator.shutdown()
    }

    @Test
            // tag::launcher[]
    fun main() {
        Kotlin.service {
            name = ":name" // <1>
            packages = "org.maxur.mserv.sample"  // <2>
            properties { format = "hocon" }      // <3>
            services += rest { } // <4>
            beforeStart += this@MicroServiceKotlinClientIT::beforeStart // <5>
            afterStop += this@MicroServiceKotlinClientIT::afterStop
        }.start() // <6>
    }
    // end::launcher[]

    private fun afterStop(service: WebServer) {
        assertThat(service).isNotNull()
    }

    private fun beforeStart(config: PropertiesSource) {
        assertThat(config).isNotNull()
        assertThat(config.format).isEqualToIgnoringCase("Hocon")
    }

}