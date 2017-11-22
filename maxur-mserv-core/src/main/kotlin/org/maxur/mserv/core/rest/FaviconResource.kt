package org.maxur.mserv.core.rest

import java.io.ByteArrayInputStream
import java.io.IOException
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.Response

/**
 * The type Favicon resource.
 *
 * @author Maxim Yunusov
 * @version 1.0
 * @since <pre>11/29/13</pre>
 */
@Path("/")
open class FaviconResource {

    /**
     * Gets favicon.
     *
     * @param fileName the file name
     * @return the favicon
     * @throws IOException the io exception
     */
    @GET
    @Path("/{fileName: .*ico}")
    @Produces("image/x-icon")
    fun favicon(@PathParam("fileName") fileName: String) = (
        loadImage(fileName)
            ?.let { Response.ok(ByteArrayInputStream(it)) }
            ?: Response.status(Response.Status.NOT_FOUND)
        ).build()

    private fun loadImage(fileName: String) =
        FaviconResource::class.java.getResource("/$fileName")?.let {
            val conn = it.openConnection()
            val inConnectionReader = conn.getInputStream()
            val size = conn.contentLength
            val imageData = ByteArray(size)
            inConnectionReader.read(imageData, 0, size)
            imageData
        }
}