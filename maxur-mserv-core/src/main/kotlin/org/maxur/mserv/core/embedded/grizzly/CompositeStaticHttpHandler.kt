package org.maxur.mserv.core.embedded.grizzly

import org.glassfish.grizzly.http.server.HttpHandler
import org.glassfish.grizzly.http.server.Request
import org.glassfish.grizzly.http.server.Response
import org.maxur.mserv.core.embedded.properties.StaticContent

class CompositeStaticHttpHandler(
        val fileHandle: StaticHttpHandler,
        val clHandle: CLStaticHttpHandler
) : AbstractStaticHttpHandler() {

    companion object {
        fun make(content: StaticContent): HttpHandler {
            val fileContent = content.fileContent()
            val clContent = content.clContent()
            if (!fileContent.roots.isEmpty() && !clContent.roots.isEmpty()) {
                val fileHandle: StaticHttpHandler
                        = StaticHttpHandler(content.fileContent())
                val clHandle: CLStaticHttpHandler
                        = CLStaticHttpHandler(CompositeStaticHttpHandler::class.java.classLoader, content)
                return CompositeStaticHttpHandler(fileHandle, clHandle)
            } else if (!fileContent.roots.isEmpty()) {
                return StaticHttpHandler(content.fileContent())
            } else if (!clContent.roots.isEmpty()) {
                return CLStaticHttpHandler(CompositeStaticHttpHandler::class.java.classLoader, content)
            } else {
                throw IllegalStateException("Root of static context is not fond")
            }
        }
    }

    override fun handle(uri: String, request: Request, response: Response): Boolean =
            clHandle.handle(uri, request, response) || fileHandle.handle(uri, request, response)

}

