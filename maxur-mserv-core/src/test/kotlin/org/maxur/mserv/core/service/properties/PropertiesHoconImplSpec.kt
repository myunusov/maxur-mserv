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


class PropertiesHoconImplSpec : Spek({

    describe("a Properties Source as Hocon File") {

        fun hocon(uri: URI?= null, root: String? = null): PropertiesSource = object : PropertiesSource {
            override val format: String? get() = "Hocon"
            override val uri: URI? get() = uri
            override val rootKey: String? get() = root
        }

        context("Load properties source by url") {
            it("should return opened source with url by default") {
                val sut = PropertiesSourceHoconImpl(hocon())
                sut.should.be.not.`null`
                (sut as PropertiesSource).format.should.be.equal("Hocon")
                sut.uri.should.be.satisfy { it.toString().endsWith("application.conf") }
            }
            it("should return opened source with classpath url") {
                val sut = PropertiesSourceHoconImpl(hocon(URI("classpath://application.conf")))
                sut.should.be.not.`null`
                (sut as PropertiesSource).format.should.be.equal("Hocon")
                sut.uri.should.be.satisfy { it.toString().endsWith("application.conf") }
            }
            it("should return opened source with url by default") {
                val path = PropertiesHoconImplSpec::class.java.getResource("/application.conf").path
                val sut = PropertiesSourceHoconImpl(hocon(URL(path).toURI()))
                sut.should.be.not.`null`
                (sut as PropertiesSource).format.should.be.equal("Hocon")
                sut.uri.should.be.satisfy { it.toString().endsWith("application.conf") }
            }
        }

        context("Load properties source from file") {
            val sut = PropertiesSourceHoconImpl(hocon())
            it("should return value of properties by it's key") {
                sut.asString("name").should.be.equal("μService")
                sut.read("name", String::class).should.be.equal("μService")
                sut.asInteger("id").should.be.equal(1)
                sut.read("id", Integer::class).should.be.equal(1 as Integer)
                sut.asLong("id").should.be.equal(1L)
                sut.read("id", Long::class).should.be.equal(1L)
                sut.asURI("url").should.be.equal(URI("file:///file.txt"))
                sut.read("url", URI::class).should.be.equal(URI("file:///file.txt"))

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