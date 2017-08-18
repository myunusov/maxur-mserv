package org.maxur.mserv.doc

import org.assertj.core.api.Assertions.assertThat
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.maxur.mserv.core.Locator
import org.maxur.mserv.core.TestLocatorHolder
import org.maxur.mserv.core.domain.BaseService
import org.maxur.mserv.core.embedded.WebServer
import org.maxur.mserv.core.service.msbuilder.Kotlin
import org.maxur.mserv.core.service.properties.PropertiesSource
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MicroServiceKotlinClientIT {

    private var service1: BaseService? = null

    companion object {
        @JvmStatic
        @BeforeClass
        fun beforeClass() {
            Locator.holder = TestLocatorHolder
        }
    }

    @Test
    fun main() {
        // tag::launcher[]
        Kotlin.service {
            name = ":name" // <1>
            packages = "org.maxur.mserv.sample"  // <2>
            properties { format = "hocon" }      // <3>
            services += rest { } // <4>
            beforeStart += this@MicroServiceKotlinClientIT::beforeStart // <5>
            afterStop += this@MicroServiceKotlinClientIT::afterStop
        }.start() // <6>
        // end::launcher[]
        service1?.stop()
        Locator.shutdown()
    }

    private fun afterStop(service: WebServer) {
        assertThat(service).isNotNull()
    }

    private fun beforeStart(service: BaseService, config: PropertiesSource) {
        service1 = service
        assertThat(service).isNotNull()
        assertThat(config).isNotNull()
        assertThat(config.format).isEqualToIgnoringCase("Hocon")
    }

}