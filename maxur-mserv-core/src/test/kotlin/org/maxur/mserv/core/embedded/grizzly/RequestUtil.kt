package org.maxur.mserv.core.embedded.grizzly

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import org.glassfish.grizzly.Buffer
import org.glassfish.grizzly.Connection
import org.glassfish.grizzly.filterchain.FilterChain
import org.glassfish.grizzly.filterchain.FilterChainContext
import org.glassfish.grizzly.http.HttpResponsePacket
import org.glassfish.grizzly.http.Method
import org.glassfish.grizzly.http.io.NIOOutputStream
import org.glassfish.grizzly.http.io.OutputBuffer
import org.glassfish.grizzly.http.server.Request
import org.glassfish.grizzly.http.server.Response
import org.glassfish.grizzly.memory.MemoryManager

import org.mockito.Mockito

/**
 * @author myunusov
 * @version 1.0
 * @since <pre>12.07.2017</pre>
 */
object RequestUtil {

    fun resreq(uri: String, method: Method = Method.GET): Pair<Response, Request> {

        val chain = mock<FilterChain> {
            on { size } doReturn 0
        }

        val chainContext = mock<FilterChainContext> {
            on { filterChain } doReturn chain
            on { connection } doReturn mock<Connection<Any>>(name = Connection::class.qualifiedName!!)
            on { memoryManager } doReturn mock<MemoryManager<Buffer>>(name = MemoryManager::class.qualifiedName!!)
        }

        val responsePacket = mock<HttpResponsePacket> {
            on { isContentTypeSet } doReturn false
        }
        
        val response = mock<Response> {
            on { response } doReturn responsePacket
            on { isSendFileEnabled } doReturn true
            on { outputBuffer } doReturn mock<OutputBuffer>()
            on { nioOutputStream } doReturn mock<NIOOutputStream>()
        }
        
        val request = mock<Request> {
            on { getResponse() } doReturn response
            on { decodedRequestURI } doReturn uri
            on { getMethod() } doReturn method
            on { context } doReturn chainContext
        }

        Mockito.`when`(response.request).thenReturn(request)

        return Pair(response, request)
    }
}
