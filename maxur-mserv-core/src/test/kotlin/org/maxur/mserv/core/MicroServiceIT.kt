package org.maxur.mserv.core

import org.assertj.core.api.Assertions.assertThat
import org.junit.BeforeClass
import org.junit.Test
import org.maxur.mserv.core.domain.BaseService
import org.maxur.mserv.core.embedded.WebServer
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
            packages = "org.maxur.mserv.sample"
            properties { format = "hocon" }
            services += rest { }
            beforeStart += this@MicroServiceIT::beforeStartKt
            afterStop += this@MicroServiceIT::afterStopKt
        }.start()
        serviceToKotlin?.stop()
        Locator.shutdown()
    }

    private fun afterStopKt(service: WebServer) {
        assertThat(service).isNotNull()
    }

    private fun beforeStartKt(service: BaseService, config: PropertiesSource) {
        serviceToKotlin = service
        assertThat(service).isNotNull()
        assertThat(config).isNotNull()
        assertThat(config.format).isEqualToIgnoringCase("Hocon")
    }

    @Test
    fun javaMain() {
        Java.service()
                .name(":name")
                .packages("org.maxur.mserv.sample")
                .properties("hocon")
                .rest()
                .beforeStart(Consumer { this.beforeStartJava(it) })
                .afterStop(Consumer { this.afterStopJava(it) })
                .start()
        serviceToJava?.stop()
        Locator.shutdown()
    }

    private fun afterStopJava(service: BaseService) {
        assertThat(service).isNotNull()
    }

    private fun beforeStartJava(service: BaseService) {
        serviceToJava = service
        assertThat(service).isNotNull()
        val locator = service.locator
        val config = locator.service(PropertiesSource::class.java)
        assertThat(config).isNotNull()
        assertThat(config!!.format).isEqualToIgnoringCase("Hocon")
    }

}