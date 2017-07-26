package org.maxur.mserv.core.service.properties

import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.winterbe.expekt.should
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import java.net.URI
import java.net.URL
import java.time.Duration
import java.time.temporal.ChronoUnit
import kotlin.test.assertFailsWith


class PropertiesSourceJacksonImplSpec : Spek({

    describe("a Properties Source as Yaml File") {

        context("Load properties source by url") {

            it("should return opened source with url by default") {
                val rawSource = PropertiesSource.make("Yaml")
                val sut = PropertiesSourceJacksonImpl(YAMLFactory(), "yaml", rawSource)
                sut.should.be.not.`null`
                sut.format.should.be.equal("Yaml")
                sut.isOpened.should.be.`true`
                sut.uri.should.be.satisfy { it.toString().endsWith("application.yaml") }
            }
            it("should return opened source with classpath url") {
                val rawSource = PropertiesSource.make("Yaml", URI("classpath://application.yaml"))
                val sut = PropertiesSourceJacksonImpl(YAMLFactory(), "yaml", rawSource)
                sut.should.be.not.`null`
                sut.format.should.be.equal("Yaml")
                sut.isOpened.should.be.`true`
                sut.uri.should.be.satisfy { it.toString().endsWith("application.yaml") }
            }
            it("should return opened source with url by default") {
                val path = PropertiesSourceHoconImplSpec::class.java.getResource("/application.yaml").path
                val rawSource = PropertiesSource.make("Yaml", URL(path).toURI())
                val sut = PropertiesSourceJacksonImpl(YAMLFactory(), "yaml", rawSource)
                sut.should.be.not.`null`
                sut.format.should.be.equal("Yaml")
                sut.isOpened.should.be.`true`
                sut.uri.should.be.satisfy { it.toString().endsWith("application.yaml") }
            }
        }
        context("Load properties source from file") {
            val rawSource = PropertiesSource.make("Yaml")
            val sut = PropertiesSourceJacksonImpl(YAMLFactory(), "yaml", rawSource)

            it("should return value of properties by it's key") {
                sut.asString("name").should.be.equal("μService")
                sut.asURI("url").should.be.equal(URI("file:///file.txt"))
                sut.asLong("id").should.be.equal(1L)
                sut.asInteger("id").should.be.equal(1)
                sut.read("id", Double::class).should.be.equal(1.0)
                sut.read("time", Duration::class).should.be.equal(Duration.of(1, ChronoUnit.SECONDS))
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