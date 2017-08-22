package org.maxur.mserv.core

import org.assertj.core.api.Assertions.assertThat
import org.junit.BeforeClass
import org.junit.Test
import org.maxur.mserv.core.domain.BaseService
import org.maxur.mserv.core.embedded.EmbeddedService
import org.maxur.mserv.core.sample.SampleService
import org.maxur.mserv.core.service.msbuilder.Java
import org.maxur.mserv.core.service.msbuilder.Kotlin
import org.maxur.mserv.core.service.properties.PropertiesSource
import java.util.function.Consumer

class MicroServiceIT {

    companion object {
        @JvmStatic
        @BeforeClass
        fun beforeClass() {
            Locator.holder = TestLocatorHolder
        }
    }

    private var serviceToKotlin: BaseService? = null
    private var serviceToJava: BaseService? = null

    @Test
    fun kotlinMain() {
        Kotlin.service {
            name = ":name"
            packages = "org.maxur.mserv.core.sample"
            properties { format = "hocon" }
            services += rest { }
            afterStart += this@MicroServiceIT::afterStartKt
            beforeStop += this@MicroServiceIT::beforeStopKt
            onError += { ex -> throw ex }
        }.start()
        serviceToKotlin?.stop()
        Locator.shutdown()
    }

    fun beforeStopKt(service: BaseService, embeddedService: EmbeddedService) {
        assertThat(service).isNotNull()
        assertThat(embeddedService).isNotNull()
    }

    fun afterStartKt(service: BaseService, config: PropertiesSource) {
        serviceToKotlin = service
        assertThat(service).isNotNull()
        assertThat(config).isNotNull()
        assertThat(config.format).isEqualToIgnoringCase("Hocon")
        val sampleService = Locator.service(SampleService::class)
        assertThat(sampleService).isNotNull()
        assertThat(sampleService!!.name).isEqualToIgnoringWhitespace("μService")
    }

    @Test
    fun javaMain() {
        Java.service()
                .name(":name")
                .packages("org.maxur.mserv.core.sample")
                .properties("hocon")
                .rest()
                .afterStart(Consumer { this.afterStartJava(it) })
                .beforeStop(Consumer { this.beforeStopJava(it) })
                .onError (Consumer { ex -> throw ex })
                .start()
        serviceToJava?.stop()
        Locator.shutdown()
    }

    private fun beforeStopJava(service: BaseService) {
        assertThat(service).isNotNull()
    }

    private fun afterStartJava(service: BaseService) {
        serviceToJava = service
        assertThat(service).isNotNull()
        val locator = service.locator
        val config = locator.service(PropertiesSource::class.java)
        assertThat(config).isNotNull()
        assertThat(config!!.format).isEqualToIgnoringCase("Hocon")
        val sampleService = Locator.service(SampleService::class)
        assertThat(sampleService).isNotNull()
        assertThat(sampleService!!.name).isEqualToIgnoringWhitespace("μService")

    }

}