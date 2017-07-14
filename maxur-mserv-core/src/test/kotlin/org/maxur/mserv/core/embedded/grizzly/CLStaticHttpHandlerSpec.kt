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


class CLStaticHttpHandlerSpec : Spek({

    describe("a CLStaticHttpHandler") {

        on("Create CLStaticHttpHandler with folder") {
            it("should return new CLStaticHttpHandler  instance") {
                val content = StaticContent("web", "classpath:/web/")
                val handler = StaticHttpHandler(content)
                handler.should.be.not.`null`
            }
        }

        on("valid request of jar") {
            it("should return new CLStaticHttpHandler  instance") {
                val content = StaticContent("web", "classpath:/META-INF/resources/webjars/swagger-ui/3.0.17/")
                val handler = StaticHttpHandler(content)
                val (response, request) = RequestUtil.resreq("/")
                handler.service(request, response)
                verify(response).setStatus(HttpStatus.OK_200)
            }
        }

        on("valid request of file from jar") {
            it("should return new CLStaticHttpHandler  instance") {
                val content = StaticContent("web", "classpath:/META-INF/resources/webjars/swagger-ui/3.0.17/")
                val handler = StaticHttpHandler(content)
                val (response, request) = RequestUtil.resreq("/index.html")
                handler.service(request, response)
                verify(response).setStatus(HttpStatus.OK_200)
            }
        }

        on("request of invalid file from jar") {
            it("should return new CLStaticHttpHandler  instance") {
                val content = StaticContent("web", "classpath:/META-INF/resources/webjars/swagger-ui/3.0.17/")
                val handler = StaticHttpHandler(content)
                val (response, request) = RequestUtil.resreq("/error.html")
                handler.service(request, response)
                verify(response).sendError(404)
            }
        }


        on("valid request of root folder") {
            it("should return status 200") {
                val content = StaticContent("web", "classpath:/web/")
                val handler = StaticHttpHandler(content)
                val (response, request) = RequestUtil.resreq("/")
                handler.service(request, response)
                verify(response).setStatus(HttpStatus.OK_200)
            }
        }

        on("invalid request of root folder") {
            it("should return status 404") {
                val content = StaticContent("web", "classpath:/web/")
                val handler = StaticHttpHandler(content)
                val (response, request) = RequestUtil.resreq("/error/")
                handler.service(request, response)
                verify(response).sendError(404)
            }
        }

        on("request of invalid root folder") {
            it("should return status 404") {
                val content = StaticContent("web", "classpath:/error/")
                val handler = StaticHttpHandler(content)
                val (response, request) = RequestUtil.resreq("/")
                handler.service(request, response)
                verify(response).sendError(404)
            }
        }

        on("valid request of root folder with invalid method") {
            it("should return status 404") {
                val content = StaticContent("web", "classpath:/web/")
                val handler = StaticHttpHandler(content)
                val (response, request) = RequestUtil.resreq("/", Method.POST)
                handler.service(request, response)
                verify(response).setStatus(HttpStatus.METHOD_NOT_ALLOWED_405)
            }
        }
    }

})

