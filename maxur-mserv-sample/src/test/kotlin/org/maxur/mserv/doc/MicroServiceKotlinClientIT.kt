package org.maxur.mserv.doc

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.maxur.mserv.core.builder.Kotlin
import org.maxur.mserv.core.domain.BaseService
import org.maxur.mserv.core.embedded.EmbeddedService
import org.maxur.mserv.core.kotlin.Locator
import org.maxur.mserv.core.service.properties.Properties

class MicroServiceKotlinClientIT {

    private var service1: BaseService? = null

    @Test
    fun main() {
        // tag::launcher[]
        Kotlin.service {
            name = ":name" // <1>
            packages += "org.maxur.mserv.sample"  // <2>
            properties += file { format = "hocon" }      // <3>
            services += rest { } // <4>
            afterStart += this@MicroServiceKotlinClientIT::afterStart // <5>
            beforeStop += this@MicroServiceKotlinClientIT::beforeStop
        }.start() // <6>
        // end::launcher[]
        service1?.stop()
        Locator.stop()
    }

    fun beforeStop(service: EmbeddedService) {
        assertThat(service).isNotNull()
    }

    fun afterStart(service: BaseService, config: Properties) {
        service1 = service
        assertThat(service).isNotNull()
        assertThat(config).isNotNull()
        assertThat(config.sources.get(0).format).isEqualToIgnoringCase("Hocon")
    }

}