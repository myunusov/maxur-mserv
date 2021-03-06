package org.maxur.mserv.frame.service.properties

import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import org.maxur.mserv.frame.LocatorImpl
import org.maxur.mserv.frame.TestLocatorHolder
import java.net.URI
import java.net.URL
import java.time.Duration
import java.time.temporal.ChronoUnit
import kotlin.test.assertFailsWith

@RunWith(JUnitPlatform::class)
class PropertiesJacksonImplSpec : Spek({

    describe("a Properties Source as Yaml File") {

        beforeEachTest {
            LocatorImpl.holder = TestLocatorHolder
        }

        context("Load properties source by url") {

            it("should return opened source with url by default") {
                val sut = PropertiesSourceJacksonImpl(YAMLFactory(), "yaml")
                assertThat(sut).isNotNull()
                assertThat(sut.format).isEqualTo("Yaml")
                assertThat(sut.uri.toString()).endsWith("application.yaml")
            }
            it("should return opened source with classpath url") {
                val sut = PropertiesSourceJacksonImpl(
                        YAMLFactory(),
                        "yaml",
                        URI("classpath://application.yaml")
                )
                assertThat(sut).isNotNull()
                assertThat(sut.format).isEqualTo("Yaml")
                assertThat(sut.uri.toString()).endsWith("application.yaml")
            }
            it("should return opened source with url by default") {
                val uri = URL(PropertiesJacksonImplSpec::class.java.getResource("/application.yaml").path).toURI()
                val sut = PropertiesSourceJacksonImpl(YAMLFactory(), "yaml", uri)
                assertThat(sut).isNotNull()
                assertThat(sut.format).isEqualTo("Yaml")
                assertThat(sut.uri.toString()).endsWith("application.yaml")
            }
        }
        context("Load properties source from file") {
            val sut = PropertiesSourceJacksonImpl(YAMLFactory(), "yaml")

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