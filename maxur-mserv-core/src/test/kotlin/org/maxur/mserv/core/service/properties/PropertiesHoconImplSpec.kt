package org.maxur.mserv.core.service.properties

import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import java.net.URI
import java.net.URL
import java.time.Duration
import java.time.temporal.ChronoUnit
import kotlin.test.assertFailsWith

@RunWith(JUnitPlatform::class)
class PropertiesHoconImplSpec : Spek({

    describe("a Properties Source as Hocon File") {

        fun hocon(uri: URI?= null, root: String? = null): PropertiesSource
                = object : PropertiesSource ("Hocon", uri, root) {}

        context("Load properties source by url") {
            it("should return opened source with url by default") {
                val sut = PropertiesSourceHoconImpl(hocon())
                assertThat(sut).isNotNull()
                assertThat(sut.format).isEqualTo("Hocon")
                assertThat(sut.uri.toString()).endsWith("application.conf")
            }
            it("should return opened source with classpath url") {
                val sut = PropertiesSourceHoconImpl(hocon(URI("classpath://application.conf")))
                assertThat(sut).isNotNull()
                assertThat(sut.format).isEqualTo("Hocon")
                assertThat(sut.uri.toString()).endsWith("application.conf")
            }
            it("should return opened source with url by default") {
                val path = PropertiesHoconImplSpec::class.java.getResource("/application.conf").path
                val sut = PropertiesSourceHoconImpl(hocon(URL(path).toURI()))
                assertThat(sut).isNotNull()
                assertThat(sut.format).isEqualTo("Hocon")
                assertThat(sut.uri.toString()).endsWith("application.conf")
            }
        }

        context("Load properties source from file") {
            val sut = PropertiesSourceHoconImpl(hocon())
            it("should return value of properties by it's key") {
                assertThat(sut.asString("name")).isEqualTo("μService")
                assertThat(sut.read("name", String::class)).isEqualTo("μService")
                assertThat(sut.asInteger("id")).isEqualTo(1)
                assertThat(sut.read("id", Integer::class)).isEqualTo(1)
                assertThat(sut.asLong("id")).isEqualTo(1L)
                assertThat(sut.read("id", Long::class)).isEqualTo(1L)
                assertThat(sut.asURI("url")).isEqualTo(URI("file:///file.txt"))
                assertThat(sut.read("url", URI::class)).isEqualTo(URI("file:///file.txt"))
                assertThat(sut.read("id", Double::class)).isEqualTo(1.0)
                assertThat(sut.read("time", Duration::class)).isEqualTo(Duration.of(1, ChronoUnit.SECONDS))
            }
            it("should throw exception when properties is not found") {
                assertFailsWith<IllegalStateException> {
                    sut.asInteger("error")
                }
            }
            it("should throw exception when properties is not parsed") {
                assertFailsWith<IllegalStateException> {
                    sut.read("id", PropertiesSourceHoconImpl::class)
                }
            }            
        }

    }
    
})