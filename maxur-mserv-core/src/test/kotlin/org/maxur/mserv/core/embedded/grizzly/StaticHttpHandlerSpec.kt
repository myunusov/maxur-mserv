package org.maxur.mserv.core.embedded.grizzly

import com.nhaarman.mockito_kotlin.verify
import org.assertj.core.api.Assertions.assertThat
import org.glassfish.grizzly.http.Method
import org.glassfish.grizzly.http.util.HttpStatus
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import org.maxur.mserv.core.embedded.properties.WebAppProperties
import org.maxur.mserv.core.relativePath

@RunWith(JUnitPlatform::class)
class StaticHttpHandlerSpec : Spek({

    describe("a StaticHttpHandler") {

        val webFolder = this::class.java.getResource("/web")

        context("Create StaticHttpHandler") {
            it("should return new StaticHttpHandler instance with folder from classpath") {
                val handler = StaticHttpHandler("web", "classpath:/web/")
                assertThat(handler).isNotNull()
            }

            it("should return new StaticHttpHandler instance with folder from file system") {
                val handler = StaticHttpHandler("web", "src/test/resources/web/")
                assertThat(handler).isNotNull()
            }
        }

        context("Create StaticHttpHandler on jar and send request") {

            val handler = StaticHttpHandler("web", WebAppProperties.SWAGGER_URL)

            it("should return status 200 on request folder (root)") {
                val (response, request) = RequestUtil.resreq("/")
                handler.service(request, response)
                verify(response).setStatus(HttpStatus.OK_200)
            }
            it("should return status 200 on request file") {
                val (response, request) = RequestUtil.resreq("/index.html")
                handler.service(request, response)
                verify(response).setStatus(HttpStatus.OK_200)
            }
            it("should return status 404 on request invalid file") {
                val (response, request) = RequestUtil.resreq("/error.html")
                handler.service(request, response)
                verify(response).sendError(404)
            }
        }

        context("Create StaticHttpHandler on classpath and send request") {

            val handler = StaticHttpHandler("web", "classpath:/web/")

            it("should return status 200 from classpath") {
                val (response, request) = RequestUtil.resreq("/")
                handler.service(request, response)
                verify(response).setStatus(HttpStatus.OK_200)
            }
            it("should return status 404  on request invalid folder") {
                val (response, request) = RequestUtil.resreq("/error/")
                handler.service(request, response)
                verify(response).sendError(404)
            }
            it("should return status 405 on request by invalid method") {
                val (response, request) = RequestUtil.resreq("/", Method.POST)
                handler.service(request, response)
                verify(response).setStatus(HttpStatus.METHOD_NOT_ALLOWED_405)
            }
        }

        context("Create StaticHttpHandler on file system folder by absolute path and send request") {
            val handler = StaticHttpHandler("web", "$webFolder/")

            it("should return status 200 on request file") {
                val (response, request) = RequestUtil.resreq("/index.html")
                handler.service(request, response)
                verify(response).setStatus(HttpStatus.OK_200)
            }
        }

        context("Create StaticHttpHandler on file system folder and send request") {

            val handler = StaticHttpHandler("web", webFolder.relativePath())

            it("should return status 200 on request folder (root)") {
                val (response, request) = RequestUtil.resreq("/")
                handler.service(request, response)
                verify(response).setStatus(HttpStatus.OK_200)
            }
            it("should return status 200 on request file") {
                val (response, request) = RequestUtil.resreq("/index.html")
                handler.service(request, response)
                verify(response).setStatus(HttpStatus.OK_200)
            }
            it("should return status 200 and default page on empty request") {
                val (response, request) = RequestUtil.resreq("")
                handler.service(request, response)
                verify(response).setStatus(HttpStatus.OK_200)
            }
            it("should return status 301 and redirect request without tailed slash") {
                val (response, request) = RequestUtil.resreq("folder")
                handler.service(request, response)
                verify(response).setStatus(HttpStatus.MOVED_PERMANENTLY_301)
            }
        }

        context("Create StaticHttpHandler on invalid file system folder and send request") {
            it("should return status 404 on request invalid folder") {
                val handler = StaticHttpHandler("web", "classpath:/error/")
                val (response, request) = RequestUtil.resreq("/")
                handler.service(request, response)
                verify(response).sendError(404)
            }
        }
    }
})

