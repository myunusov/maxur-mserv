package org.maxur.mserv.core.embedded.grizzly

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import org.glassfish.grizzly.filterchain.FilterChain
import org.glassfish.grizzly.filterchain.FilterChainContext
import org.glassfish.grizzly.http.HttpResponsePacket
import org.glassfish.grizzly.http.Method
import org.glassfish.grizzly.http.io.OutputBuffer
import org.glassfish.grizzly.http.server.Request
import org.glassfish.grizzly.http.server.Response

/**
 * @author myunusov
 * @version 1.0
 * @since <pre>12.07.2017</pre>
 */
object RequestUtil {

    fun resreq(uri: String, method: Method = Method.GET): Pair<Response, Request> {
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
}