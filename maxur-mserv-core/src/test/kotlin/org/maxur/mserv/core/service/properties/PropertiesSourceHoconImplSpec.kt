package org.maxur.mserv.core.service.properties

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


class PropertiesSourceHoconImplSpec : Spek({

    describe("a Properties Source as Hocon File") {

        context("Load properties source by url") {
            it("should return opened source with url by default") {
                val rawSource = PropertiesSource.make("Hocon")
                val sut = PropertiesSourceHoconImpl(rawSource)
                sut.should.be.not.`null`
                sut.format.should.be.equal("Hocon")
                sut.isOpened.should.be.`true`
                sut.uri.should.be.satisfy { it.toString().endsWith("application.conf") }
            }
            it("should return opened source with classpath url") {
                val rawSource = PropertiesSource.make("Hocon", URI("classpath://application.conf"))
                val sut = PropertiesSourceHoconImpl(rawSource)
                sut.should.be.not.`null`
                sut.format.should.be.equal("Hocon")
                sut.isOpened.should.be.`true`
                sut.uri.should.be.satisfy { it.toString().endsWith("application.conf") }
            }
            it("should return opened source with url by default") {
                val path = PropertiesSourceHoconImplSpec::class.java.getResource("/application.conf").path
                val rawSource = PropertiesSource.make("Hocon", URL(path).toURI())
                val sut = PropertiesSourceHoconImpl(rawSource)
                sut.should.be.not.`null`
                sut.format.should.be.equal("Hocon")
                sut.isOpened.should.be.`true`
                sut.uri.should.be.satisfy { it.toString().endsWith("application.conf") }
            }
        }

        context("Load properties source from file") {
            val rawSource = PropertiesSource.make("Hocon")
            val sut = PropertiesSourceHoconImpl(rawSource)

            it("should return value of properties by it's key") {
                sut.asString("name").should.be.equal("μService")
                sut.asLong("id").should.be.equal(1L)
                sut.asInteger("id").should.be.equal(1)
                sut.read("id", Double::class).should.be.equal(1.0)
                sut.read("time", Duration::class).should.be.equal(Duration.of(1, ChronoUnit.SECONDS))
                sut.asURI("url").should.be.equal(URI("file:///file.txt"))
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