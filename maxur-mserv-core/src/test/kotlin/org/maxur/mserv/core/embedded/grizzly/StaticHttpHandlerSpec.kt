package org.maxur.mserv.core.embedded.grizzly

import com.nhaarman.mockito_kotlin.verify
import com.winterbe.expekt.should
import org.glassfish.grizzly.http.Method
import org.glassfish.grizzly.http.util.HttpStatus
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.maxur.mserv.core.embedded.properties.StaticContent

/**
 * @author myunusov
 * @version 1.0
 * @since <pre>12.07.2017</pre>
 */
class StaticHttpHandlerSpec: Spek({

    describe("a StaticHttpHandler") {

        on("Create StaticHttpHandler") {
            it("should return new StaticHttpHandler instance") {
                val content = StaticContent("web", "src/test/resources/web/")
                val handler = StaticHttpHandler(content)
                handler.should.be.not.`null`
            }
        }

        on("valid request of root folder") {
            it("should return status 200") {
                val content = StaticContent("web", "src/test/resources/web/")
                val handler = StaticHttpHandler(content)
                val (response, request) = RequestUtil.resreq("/")
                handler.service(request, response)
                verify(response).setStatus(HttpStatus.OK_200)
            }
        }

        on("valid request of page") {
            it("should return status 200") {
                val content = StaticContent("web", "src/test/resources/web/")
                val handler = StaticHttpHandler(content)
                val (response, request) = RequestUtil.resreq("/index.html")
                handler.service(request, response)
                verify(response).setStatus(HttpStatus.OK_200)
            }
        }

        on("valid request of page by absolute path") {
            it("should return status 200") {
                val folder = System.getProperty("user.dir").replace('\\', '/');
                val content = StaticContent("web", "file:///$folder/src/test/resources/web/")
                val handler = StaticHttpHandler(content)
                val (response, request) = RequestUtil.resreq("/index.html")
                handler.service(request, response)
                verify(response).setStatus(HttpStatus.OK_200)
            }
        }

        on("valid request of root folder with redirect") {
            it("should return status 200") {
                val content = StaticContent("web", "src/test/resources/web/")
                val handler = StaticHttpHandler(content)
                val (response, request) = RequestUtil.resreq("")
                handler.service(request, response)
                verify(response).setStatus(HttpStatus.OK_200)
            }
        }

        on("invalid request of root folder") {
            it("should return status 404") {
                val content = StaticContent("web", "src/test/resources/web/")
                val handler = StaticHttpHandler(content)
                val (response, request) = RequestUtil.resreq("/error/")
                handler.service(request, response)
                verify(response).sendError(404)
            }
        }

        on("request of invalid root folder") {
            it("should return status 404") {
                val content = StaticContent("web", "src/test/resources/error/")
                val handler = StaticHttpHandler(content)
                val (response, request) = RequestUtil.resreq("/")
                handler.service(request, response)
                verify(response).sendError(404)
            }
        }

        on("valid request of root folder with invalid method") {
            it("should return status 404") {
                val content = StaticContent("web", "src/test/resources/web/")
                val handler = StaticHttpHandler(content)
                val (response, request) = RequestUtil.resreq("/", Method.POST)
                handler.service(request, response)
                verify(response).setStatus(HttpStatus.METHOD_NOT_ALLOWED_405)
            }
        }
    }

})