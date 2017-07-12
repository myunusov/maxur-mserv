package org.maxur.mserv.core.embedded.grizzly

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.glassfish.grizzly.filterchain.FilterChain
import org.glassfish.grizzly.filterchain.FilterChainContext
import org.glassfish.grizzly.http.HttpResponsePacket
import org.glassfish.grizzly.http.Method
import org.glassfish.grizzly.http.io.OutputBuffer
import org.glassfish.grizzly.http.server.Request
import org.glassfish.grizzly.http.server.Response
import org.glassfish.grizzly.http.util.HttpStatus
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.maxur.mserv.core.embedded.properties.StaticContent


class CLStaticHttpHandlerSpec : Spek({

    describe("a CLStaticHttpHandler") {
        on("Create CLStaticHttpHandler") {
            it("should return new CLStaticHttpHandler") {
                val content = StaticContent("web", "classpath:/web/")
                val handler = CLStaticHttpHandler(CompositeStaticHttpHandler::class.java.classLoader, content)
                val (response, request) = resreq("/")
                handler.service(request, response)
                verify(response).setStatus(HttpStatus.OK_200)
            }
        }
    }



})

private fun resreq(uri: String, method: Method = Method.GET): Pair<Response, Request> {
    val responsePacket = mock<HttpResponsePacket> {
        on { isContentTypeSet } doReturn false
    }
    val response = mock<Response> {
        on { request } doReturn mock<Request>()
        on { response } doReturn responsePacket
        on { isSendFileEnabled } doReturn true
        on { outputBuffer } doReturn mock<OutputBuffer>()
    }
    val chain = mock<FilterChain> {
        on { size } doReturn 0
    }
    val chainContext = mock<FilterChainContext> {
        on { filterChain } doReturn chain
    }
    val request = mock<Request> {
        on { getResponse() } doReturn response
        on { decodedRequestURI } doReturn uri
        on { getMethod() } doReturn method
        on { context } doReturn chainContext
    }
    return Pair(response, request)
}