package org.maxur.mserv.doc

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.maxur.mserv.core.Locator
import org.maxur.mserv.core.domain.BaseService
import org.maxur.mserv.core.embedded.WebServer
import org.maxur.mserv.core.service.msbuilder.Kotlin
import org.maxur.mserv.core.service.properties.PropertiesSource

class MicroServiceKotlinClientIT {

    private var service1: BaseService? = null

    @Test
    fun main() {
        // tag::launcher[]
        Kotlin.service {
            name = ":name" // <1>
            packages = "org.maxur.mserv.sample"  // <2>
            properties { format = "hocon" }      // <3>
            services += rest { } // <4>
            afterStart += this@MicroServiceKotlinClientIT::afterStart // <5>
            beforeStop += this@MicroServiceKotlinClientIT::beforeStop
        }.start() // <6>
        // end::launcher[]
        service1?.stop()
        Locator.shutdown()
    }

    private fun beforeStop(service: WebServer) {
        assertThat(service).isNotNull()
    }

    private fun afterStart(service: BaseService, config: PropertiesSource) {
        service1 = service
        assertThat(service).isNotNull()
        assertThat(config).isNotNull()
        assertThat(config.format).isEqualToIgnoringCase("Hocon")
    }

}