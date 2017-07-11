package org.maxur.mserv.core.embedded.grizzly

/**
 * @author myunusov
 * @version 1.0
 * @since <pre>20.06.2017</pre>
 */
import org.glassfish.grizzly.Buffer
import org.glassfish.grizzly.Grizzly
import org.glassfish.grizzly.WriteHandler
import org.glassfish.grizzly.http.Method
import org.glassfish.grizzly.http.io.NIOOutputStream
import org.glassfish.grizzly.http.server.HttpHandler
import org.glassfish.grizzly.http.server.Request
import org.glassfish.grizzly.http.server.Response
import org.glassfish.grizzly.http.server.StaticHttpHandlerBase
import org.glassfish.grizzly.http.util.Header
import org.glassfish.grizzly.http.util.HttpStatus
import org.glassfish.grizzly.http.util.MimeType
import org.glassfish.grizzly.memory.MemoryManager
import org.maxur.mserv.core.embedded.properties.StaticContent
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream
import java.net.*
import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.logging.Level

/**
 * [HttpHandler], which processes requests to a static resources resolved
 * by a given [ClassLoader].
 *
 * Create <tt>HttpHandler</tt>, which will handle requests
 * to the static resources resolved by the given class loader.
 *
 * @param classLoader [ClassLoader] to be used to resolve the resources
 * @param staticContent is the static content configuration
 *
 * @author Grizzly Team
 * @author Maxim Yunusov
 */
class CLStaticHttpHandler(val classLoader: ClassLoader, staticContent: StaticContent) : AbstractStaticHttpHandler() {

    private val resourceLocator: ResourceLocator = ResourceLocator(classLoader, staticContent)

    private val defaultPage: String = staticContent.page!!

    /**
     * {@inheritDoc}
     */
    @Throws(Exception::class)
    public override fun handle(resourcePath: String, request: Request, response: Response): Boolean {
        resourceLocator.find(resourcePath)?.let {
            return it.open(request, response)
        }
        fine("Resource not found $resourcePath")
        return false
    }

    inner class FileResource(resourcePath: String, val url: URL) : Resource() {

        var file: File? = null

        override val path: String = resourcePath
            get() = file?.path ?: field

        override fun init(): Boolean {
            file = respondedFile(url)
            return file != null
        }

        override fun process(request: Request, response: Response) {
            addToFileCache(request, response, file)
            StaticHttpHandlerBase.sendFile(response, file)
        }

        private fun respondedFile(url: URL): File? {
            val file = File(url.toURI())
            if (file.exists()) {
                if (file.isDirectory) {
                    val welcomeFile = File(file, "/$defaultPage")
                    if (welcomeFile.exists() && welcomeFile.isFile) {
                        return welcomeFile
                    }
                } else {
                    return file
                }
            }
            return null
        }
    }

    inner class JarResource(val url: URL) : Resource() {

        val urlConnection: URLConnection = url.openConnection()

        var filePath: String? = null

        override val path: String = url.path
            get() = filePath ?: field

        private var urlInputStream: JarURLInputStream? = null

        override fun init(): Boolean {
            val jarUrlConnection = urlConnection as JarURLConnection?
            var jarEntry: JarEntry? = jarUrlConnection!!.jarEntry
            val jarFile = jarUrlConnection.jarFile
            var iinputStream: InputStream? = jarFile.getInputStream(jarEntry)
            if (jarEntry!!.isDirectory || iinputStream == null) { // it's probably a folder
                val welcomeResource = if (jarEntry.name.endsWith("/"))
                    "${jarEntry.name}$defaultPage"
                else
                    "${jarEntry.name}/$defaultPage"

                jarEntry = jarFile.getJarEntry(welcomeResource)
                if (jarEntry != null) {
                    iinputStream = jarFile.getInputStream(jarEntry)
                }
            }
            if (iinputStream != null) {
                urlInputStream = JarURLInputStream(jarUrlConnection, jarFile, iinputStream)
                assert(jarEntry != null)
                filePath = jarEntry!!.name
                return true
            } else {
                urlInputStream?.closeJarFileIfNeeded()
                return false
            }
        }

        override fun process(request: Request, response: Response) {
            val jarFile = getJarFile(
                    // we need that because url.getPath() may have url encoded symbols,
                    // which are getting decoded when calling uri.getPath()
                    URI(url.path).path
            )
            // if it's not a jar file - we don't know what to do with that
            // so not adding it to the file cache
            addTimeStampEntryToFileCache(request, response, jarFile)
            val stream = urlInputStream ?: urlConnection.getInputStream()!!
            sendResource(response, stream)
        }

        private fun addTimeStampEntryToFileCache(req: Request, res: Response, archive: File): Boolean {
            if (isFileCacheEnabled) {
                val fcContext = req.context
                val fileCacheFilter = lookupFileCache(fcContext)
                if (fileCacheFilter != null) {
                    val fileCache = fileCacheFilter.fileCache
                    if (fileCache.isEnabled) {
                        StaticHttpHandlerBase.addCachingHeaders(res, archive)
                        fileCache.add(req.request, archive.lastModified())
                        return true
                    }
                }
            }
            return false
        }

        @Throws(MalformedURLException::class, FileNotFoundException::class)
        private fun getJarFile(path: String): File {
            val jarDelimIdx = path.indexOf("!/")
            if (jarDelimIdx == -1) {
                throw MalformedURLException("The jar file delimeter were not found")
            }

            val file = File(path.substring(0, jarDelimIdx))

            if (!file.exists() || !file.isFile) {
                throw FileNotFoundException("The jar file was not found")
            }
            return file
        }
    }

    class JarURLInputStream(
            private val jarConnection: JarURLConnection,
            private val jarFile: JarFile,
            src: InputStream
    ) : java.io.FilterInputStream(src) {

        @Throws(IOException::class)
        override fun close() {
            try {
                super.close()
            } finally {
                closeJarFileIfNeeded()
            }
        }

        @Throws(IOException::class)
        fun closeJarFileIfNeeded() {
            if (!jarConnection.useCaches) {
                jarFile.close()
            }
        }
    }


    // OSGi resource
    inner class BundleResource(var mayBeFolder: Boolean, var url: URL) : Resource() {

        var urlConnection: URLConnection = url.openConnection()

        override val path: String = url.path

        override fun init(): Boolean {
            if (mayBeFolder && urlConnection.contentLength <= 0) { // looks like a folder?
                // check if there's a welcome resource
                val welcomeUrl = classLoader.getResource("${url.path}/$defaultPage")
                if (welcomeUrl != null) {
                    url = welcomeUrl
                    urlConnection = welcomeUrl.openConnection()
                }
            }
            return true
        }

        override fun process(request: Request, response: Response) {
            sendResource(response, urlConnection.getInputStream())
        }
    }

    inner class UnknownResource(val url: URL) : Resource() {

        override val path: String = url.path

        var urlConnection: URLConnection = url.openConnection()

        override fun init(): Boolean = true

        override fun process(request: Request, response: Response) {
            sendResource(response, urlConnection.getInputStream())
        }
    }

    inner class ResourceLocator(val classLoader: ClassLoader, staticContent: StaticContent) {

        private val SLASH_STR = "/"
        private val EMPTY_STR = ""

        private val roots = makeRoots(staticContent)

        private fun makeRoots(staticContent: StaticContent): Set<String> {
            val set = staticContent.roots
                    .map { makeRoot(it) }
                    .filterNotNull()
                    .map { checkLastSlash(it) }
                    .map { skipFirstSlash(it) }
                    .toHashSet()

            if (set.isNotEmpty()) {
                return set
            } else {
                return setOf(EMPTY_STR)
            }
        }

        private fun checkLastSlash(docRoot: String): String =
                if (docRoot.endsWith(SLASH_STR)) docRoot
                else throw IllegalArgumentException("Doc root should end with slash ('/')")

        private fun makeRoot(it: URI): String? =
                when (it.scheme) {
                    "classpath" -> it.toString().substring("classpath".length + 1)
                    else -> null
                }

        //@todo #2 DEV move "check-non-slash-terminated-folders" to web-app properties
        private val CHECK_NON_SLASH_TERMINATED_FOLDERS_PROP =
                CLStaticHttpHandler::class.java.name + ".check-non-slash-terminated-folders"

        /**
         * <tt>true</tt> (default) if we want to double-check the resource requests,
         * that don't have terminating slash if they represent a folder and try
         * to retrieve a welcome resource from the folder.
         */
        private val CHECK_NON_SLASH_TERMINATED_FOLDERS =
                System.getProperty(CHECK_NON_SLASH_TERMINATED_FOLDERS_PROP) == null ||
                        java.lang.Boolean.getBoolean(CHECK_NON_SLASH_TERMINATED_FOLDERS_PROP)


        fun find(resourcePath: String): Resource? {
            var path = skipFirstSlash(resourcePath)
            if (path.isEmpty() || path.endsWith(SLASH_STR)) {
                path += defaultPage
                val url = lookupResource(path) ?: return null
                return make(path, url, false)
            } else {
                return lookupResource(path)?.let {
                    make(path, it, true)
                } ?: if (CHECK_NON_SLASH_TERMINATED_FOLDERS) {
                    // So try to add index.html to double-check.
                    // For example null will be returned for a folder inside a jar file.
                    val url = lookupResource("$path/$defaultPage") ?: return null
                    // some ClassLoaders return null if a URL points to a folder.
                    return make(path, url, false)
                } else {
                    null
                }
            }
        }

        private fun make(path: String, url: URL, mayBeFolder: Boolean): Resource = when (url.protocol) {
            "file" -> FileResource(path, url)
            "jar" -> JarResource(url)
            "bundle" -> BundleResource(mayBeFolder, url)
            else -> UnknownResource(url)
        }

        private fun skipFirstSlash(docRoot: String): String =
                when {
                    SLASH_STR == docRoot -> EMPTY_STR
                    docRoot.startsWith(SLASH_STR) -> docRoot.substring(1)
                    else -> docRoot
                }

        private fun lookupResource(resourcePath: String): URL? {
            return roots
                    .map { it + resourcePath }
                    .map { classLoader.getResource(it) }
                    .firstOrNull { it != null }
        }
    }

}


abstract class Resource {

    companion object {
        private val log = Grizzly.logger(AbstractStaticHttpHandler::class.java)
    }
    protected fun fine(msg: String) {
        if (log.isLoggable(Level.FINE)) {
            log.log(Level.FINE, msg)
        }
    }

    abstract val path: String

    // url may point to a folder or a file
    fun open(request: Request, response: Response): Boolean {
        val found = init()
        if (!found) {
            fine("Resource not found $path")
            return false
        }
        // If it's not HTTP GET - return method is not supported status
        if (Method.GET != request.method) {
            returnMethodIsNotAllowed(path, request, response)
        } else {
            pickupContentType(response, path)
            process(request, response)
        }
        return true
    }

    abstract fun init(): Boolean

    abstract fun process(request: Request, response: Response)

    /**
     *  If it's not HTTP GET - return method is not supported status
     */
    protected fun returnMethodIsNotAllowed(resource: String, request: Request, response: Response) {
        fine("File found $resource, but HTTP method ${request.method} is not allowed")
        response.setStatus(HttpStatus.METHOD_NOT_ALLOWED_405)
        response.setHeader(Header.Allow, "GET")
    }

    protected fun pickupContentType(response: Response, path: String) {
        if (!response.response.isContentTypeSet) {
            val dot = path.lastIndexOf('.')

            if (dot > 0) {
                val ext = path.substring(dot + 1)
                val ct = MimeType.get(ext)
                if (ct != null) {
                    response.contentType = ct
                }
            } else {
                response.contentType = MimeType.get("html")
            }
        }
    }

    @Throws(IOException::class)
    protected fun sendResource(response: Response, input: InputStream) {
        response.setStatus(HttpStatus.OK_200)
        response.addDateHeader(Header.Date, System.currentTimeMillis())
        val chunkSize = 8192
        response.suspend()
        val outputStream = response.nioOutputStream
        outputStream.notifyCanWrite(NonBlockingDownloadHandler(response, outputStream, input, chunkSize))
    }

    private class NonBlockingDownloadHandler internal constructor(private val response: Response,
                                                                  private val outputStream: NIOOutputStream,
                                                                  private val inputStream: InputStream,
                                                                  private val chunkSize: Int
    ) : WriteHandler {

        companion object {
            private val log = Grizzly.logger(NonBlockingDownloadHandler::class.java)
        }

        private val mm: MemoryManager<*> = response.request.context.memoryManager

        @Throws(Exception::class)
        override fun onWritePossible() {
            log.log(Level.FINE, "[onWritePossible]")
            // send CHUNK of data
            val isWriteMore = sendChunk()
            if (isWriteMore) {
                // if there are more bytes to be sent - reregister this WriteHandler
                outputStream.notifyCanWrite(this)
            }
        }

        override fun onError(t: Throwable) {
            log.log(Level.FINE, "[onError] ", t)
            response.setStatus(500, t.message)
            complete(true)
        }

        /**
         * Send next CHUNK_SIZE of file
         */
        @Throws(IOException::class)
        private fun sendChunk(): Boolean {
            // allocate Buffer
            var buffer: Buffer? = null

            if (!mm.willAllocateDirect(chunkSize)) {
                buffer = mm.allocate(chunkSize)
                val len: Int
                if (!buffer!!.isComposite) {
                    len = inputStream.read(buffer.array(),
                            buffer.position() + buffer.arrayOffset(),
                            chunkSize)
                } else {
                    val bufferArray = buffer.toBufferArray()
                    val size = bufferArray.size()
                    val buffers = bufferArray.array

                    var lenCounter = 0
                    for (i in 0..size - 1) {
                        val subBuffer = buffers[i]
                        val subBufferLen = subBuffer.remaining()
                        val justReadLen = inputStream.read(subBuffer.array(),
                                subBuffer.position() + subBuffer.arrayOffset(),
                                subBufferLen)

                        if (justReadLen > 0) {
                            lenCounter += justReadLen
                        }

                        if (justReadLen < subBufferLen) {
                            break
                        }
                    }

                    bufferArray.restore()
                    bufferArray.recycle()

                    len = if (lenCounter > 0) lenCounter else -1
                }

                if (len > 0) {
                    buffer.position(buffer.position() + len)
                } else {
                    buffer.dispose()
                    buffer = null
                }
            } else {
                val buf = ByteArray(chunkSize)
                val len = inputStream.read(buf)
                if (len > 0) {
                    buffer = mm.allocate(len)
                    buffer!!.put(buf)
                }
            }

            if (buffer == null) {
                complete(false)
                return false
            }
            // mark it available for disposal after content is written
            buffer.allowBufferDispose(true)
            buffer.trim()

            // write the Buffer
            outputStream.write(buffer)

            return true
        }

        /**
         * Complete the download
         */
        private fun complete(isError: Boolean) {
            try {
                inputStream.close()
            } catch (e: IOException) {
                if (!isError) {
                    response.setStatus(500, e.message)
                }
            }

            try {
                outputStream.close()
            } catch (e: IOException) {
                if (!isError) {
                    response.setStatus(500, e.message)
                }
            }

            if (response.isSuspended) {
                response.resume()
            } else {
                response.finish()
            }
        }
    }
}






