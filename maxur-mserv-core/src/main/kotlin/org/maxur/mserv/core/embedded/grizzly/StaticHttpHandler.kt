package org.maxur.mserv.core.embedded.grizzly

import org.glassfish.grizzly.http.server.HttpHandler
import org.glassfish.grizzly.http.server.Request
import org.glassfish.grizzly.http.server.Response
import org.maxur.mserv.core.embedded.properties.StaticContent
import java.io.File
import java.net.URI
import java.nio.file.Paths

/**
 * [HttpHandler], which processes requests to a static resources.
 *
 * @param staticContent is the static content configuration
 *
 *  with
 *      the root(s) - directories where the static resource are located
 *      the path    - url related to base url
 *      and default page (index.html by default)
 *  If the <tt>root</tt> is <tt>null</tt> - static pages won't be served by this <tt>HttpHandler</tt>
 *
 * @author Maxim Yunusov
 *
 */
class StaticHttpHandler(staticContent: StaticContent) : AbstractStaticHttpHandler() {

    /**
     * default page
     */
    private val defaultPage: String = staticContent.page ?: "index.html"

    /**
     * docRoots the list of directories where files will be serviced from.
     */
    private val docRoots: List<File>

    /**
     * <tt>true</tt> if HTTP 301 redirect shouldn't be sent when requested
     *       static resource is a directory, or <tt>false</tt> otherwise
     */
    private val isDirectorySlashOff: Boolean = false


    /**
     * Create a new instance which will look for static pages located
     * under the <tt>docRoot</tt>. If the <tt>docRoot</tt> is <tt>null</tt> -
     * static pages won't be served by this <tt>HttpHandler</tt>
     */
    init {
        docRoots = staticContent
                .roots.map { makeRoot(it) }
                .filterNotNull()

        /*
           TODO different behaviour with CLStaticHttpHandler see  
          if (set.isNotEmpty()) {
            return set
          } else {
            return setOf("")
          }
        */
    }

    private fun makeRoot(uri: URI): File? {
        return when (uri.scheme) {
            "file" -> Paths.get(uri).toFile()
            null -> File(uri.toString())
            else -> null
        }
    }

    /**
     * {@inheritDoc}
     */
    @Throws(Exception::class)
    public override fun handle(uri: String, request: Request, response: Response): Boolean {
        for (webDir in docRoots) {
            val resource: Resource = Resource(webDir, uri)
            if (!resource.exists) {
                continue
            }
            if (resource.mustBeRedirected) {
                redirectTo(response, resource.redirectedUrl!!)
            }
            val result = resource.respondedFile()
            if (result.exists()) {
                processFile(request, result, response)
                return true
            }
        }
        fine("File not found $uri")
        return false
    }


    inner class Resource(webDir: File, uri: String) {

        private val file: File = File(webDir, uri)

        val exists: Boolean = file.exists()

        private val mayBeFolder: Boolean = file.isDirectory

        val mustBeRedirected: Boolean =
                mayBeFolder && !this@StaticHttpHandler.isDirectorySlashOff && !uri.endsWith("/")

        // Redirect to the same url, but with trailing slash
        val redirectedUrl: String? = if (mustBeRedirected) "$uri/" else null

        fun respondedFile(): File {
            when {
                mayBeFolder -> return File(file, "/${this@StaticHttpHandler.defaultPage}")
                else -> return file
            }
        }
    }

}


