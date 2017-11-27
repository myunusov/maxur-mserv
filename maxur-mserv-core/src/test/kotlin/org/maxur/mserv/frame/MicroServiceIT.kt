package org.maxur.mserv.frame

import org.assertj.core.api.Assertions.assertThat
import org.junit.BeforeClass
import org.junit.Test
import org.maxur.mserv.frame.domain.BaseService
import org.maxur.mserv.frame.embedded.EmbeddedService
import org.maxur.mserv.frame.kotlin.Locator
import org.maxur.mserv.frame.runner.Java
import org.maxur.mserv.frame.runner.Kotlin
import org.maxur.mserv.frame.sample.SampleService
import org.maxur.mserv.frame.service.properties.Properties
import java.util.function.Consumer

class MicroServiceIT {

    companion object {
        @JvmStatic
        @BeforeClass
        fun beforeClass() {
            LocatorImpl.holder = TestLocatorHolder
        }
    }

    private var serviceToKotlin: BaseService? = null
    private var serviceToJava: BaseService? = null

    @Test
    fun kotlinMain() {
        Kotlin.runner {
            name = ":name"
            packages += "org.maxur.mserv.frame.sample"
            properties += file { format = "hocon" }
            services += rest { }
            afterStart += this@MicroServiceIT::afterStartKt
            beforeStop += this@MicroServiceIT::beforeStopKt
            onError += { ex -> throw ex }
        }.start()
        serviceToKotlin?.stop()
        Locator.stop()
    }

    fun beforeStopKt(service: BaseService, embeddedService: EmbeddedService) {
        assertThat(service).isNotNull()
        assertThat(embeddedService).isNotNull()
    }

    fun afterStartKt(service: BaseService, config: Properties) {
        serviceToKotlin = service
        assertThat(service).isNotNull()
        assertThat(config).isNotNull()
        assertThat(config.sources.get(0).format).isEqualToIgnoringCase("Hocon")
        val sampleService = Locator.bean(SampleService::class)
        assertThat(sampleService).isNotNull()
        assertThat(sampleService!!.name).isEqualToIgnoringWhitespace("μService")
    }

    @Test
    fun javaMain() {
        Java.runner()
            .name(":name")
            .packages("org.maxur.mserv.frame.sample")
            .properties("yaml")
            .rest()
            .afterStart(Consumer { this.afterStartJava(it) })
            .beforeStop(Consumer { this.beforeStopJava(it) })
            .onError(Consumer { ex -> throw ex })
            .start()
        serviceToJava?.stop()
        Locator.stop()
    }

    private fun beforeStopJava(service: BaseService) {
        assertThat(service).isNotNull()
    }

    private fun afterStartJava(service: BaseService) {
        serviceToJava = service
        assertThat(service).isNotNull()
        val locator = service.locator
        val config = locator.service(Properties::class.java)
        assertThat(config).isNotNull()
        assertThat(config?.sources?.get(0)?.format).isEqualToIgnoringCase("Yaml")
        val sampleService = Locator.bean(SampleService::class)
        assertThat(sampleService).isNotNull()
        assertThat(sampleService!!.name).isEqualToIgnoringWhitespace("μService")
    }
}