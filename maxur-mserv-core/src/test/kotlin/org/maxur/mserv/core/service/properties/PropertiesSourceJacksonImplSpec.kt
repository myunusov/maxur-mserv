package org.maxur.mserv.core.service.properties

import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.winterbe.expekt.should
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import java.net.URI
import java.time.Duration
import java.time.temporal.ChronoUnit
import kotlin.test.assertFailsWith


class PropertiesSourceJacksonImplSpec : Spek({

    val rawSource = PropertiesSource.make("Yaml")


    describe("a Properties Source as Yaml File") {
        
        val sut = PropertiesSourceJacksonImpl(YAMLFactory(), "yaml", rawSource)

        context("Load properties source from file") {
            it("should return new opened source") {
                sut.should.be.not.`null`
                sut.format.should.be.equal("Yaml")
                sut.isOpened.should.be.`true`
            }
            it("should return value of properties by it's key") {
                sut.asString("name").should.be.equal("μService")
                sut.asURI("url").should.be.equal(URI("file:///file.txt"))
                sut.asLong("id").should.be.equal(1L)
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