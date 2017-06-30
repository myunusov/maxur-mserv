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

    // path prefixes to be used
    private val docRoots = HashSet<String>()

    /**
     * default page
     */
    private val defaultPage: String = staticContent.page!!

    /**
     *  This staticContent is the static content configuration
     *  with
     *      the root(s) - the doc roots (path prefixes), which will be used
     *           to find resources. Effectively each docRoot will be prepended
     *           to a resource path before passing it to [ClassLoader.getResource].
     *           If no <tt>docRoots</tt> are set - the resources will be searched starting
     *           from [ClassLoader]'s root.
     *      the path    - url related to base url
     *      and default page (index.html by default)
     *  If the <tt>root</tt> is <tt>null</tt> - static pages won't be served by this <tt>HttpHandler</tt>
     *
     *  IllegalArgumentException if one of the docRoots doesn't end with slash ('/')
     */
    init {
        val roots = staticContent.roots
                .map { makeRoot(it) }
                .filterNotNull()
        if (roots.any({ !it.endsWith("/") })) {
            throw IllegalArgumentException("Doc root should end with slash ('/')")
        }
        if (roots.isNotEmpty()) {
            this.docRoots.addAll(roots)
        } else {
            this.docRoots.add("/")
        }
    }
    
    private fun makeRoot(it: URI): String? {
        return when (it.scheme) {
            "classpath" -> it.toString().substring("classpath".length + 1)
            else -> null
        }
    }

    /**
     * {@inheritDoc}
     */
    @Throws(Exception::class)
    public override fun handle(resourcePath: String, request: Request, response: Response): Boolean {

        var path = resourcePath
        if (path.startsWith(SLASH_STR)) {
            path = path.substring(1)
        }

        val mayBeFolder: Boolean
        var url: URL?

        if (path.isEmpty() || path.endsWith("/")) {
            path += defaultPage
            mayBeFolder = false
            url = lookupResource(path)
        } else {
            url = lookupResource(path)
            if (url == null && CHECK_NON_SLASH_TERMINATED_FOLDERS) {
                // So try to add index.html to double-check.
                // For example null will be returned for a folder inside a jar file.
                url = lookupResource("$path/$defaultPage")
                // some ClassLoaders return null if a URL points to a folder.
                mayBeFolder = false
            } else {
                mayBeFolder = true
            }
        }

        if (url == null) {
            fine("Resource not found $path")
            return false
        }

        // url may point to a folder or a file
        if ("file" == url.protocol) {
            return onFile(url, request, response, path)
        } else {
            return onNotFile(url, mayBeFolder, path, request, response)
        }
    }

    private fun onNotFile(url: URL, mayBeFolder: Boolean, path: String, request: Request, response: Response): Boolean {
        var url1 = url
        var urlInputStream: InputStream? = null
        var found = false
        var urlConnection: URLConnection? = url1.openConnection()
        var filePath: String? = null
        if ("jar" == url1.protocol) {
            val jarUrlConnection = urlConnection as JarURLConnection?
            var jarEntry: JarEntry? = jarUrlConnection!!.jarEntry
            val jarFile = jarUrlConnection.jarFile
            // check if this is not a folder
            // we can't rely on jarEntry.isDirectory() because of http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6233323
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
                urlInputStream = JarURLInputStream(jarUrlConnection,
                        jarFile, iinputStream)

                assert(jarEntry != null)
                filePath = jarEntry!!.name
                found = true
            } else {
                closeJarFileIfNeeded(jarUrlConnection, jarFile)
            }
        } else if ("bundle" == url1.protocol) { // OSGi resource
            // it might be either folder or file
            if (mayBeFolder && urlConnection!!.contentLength <= 0) { // looks like a folder?
                // check if there's a welcome resource
                val welcomeUrl = classLoader.getResource("${url1.path}/$defaultPage")
                if (welcomeUrl != null) {
                    url1 = welcomeUrl
                    urlConnection = welcomeUrl.openConnection()
                }
            }
            found = true
        } else {
            found = true
        }

        if (!found) {
            fine("Resource not found $path")
            return false
        }

        // If it's not HTTP GET - return method is not supported status
        if (Method.GET != request.method) {
            returnMethodIsNotAllowed(path, request, response)
            return true
        }

        pickupContentType(response, if (filePath != null) filePath else url1.path)


        assert(urlConnection != null)

        // if it's not a jar file - we don't know what to do with that
        // so not adding it to the file cache
        if ("jar" == url1.protocol) {
            val jarFile = getJarFile(
                    // we need that because url.getPath() may have url encoded symbols,
                    // which are getting decoded when calling uri.getPath()
                    URI(url1.path).path
            )

            addTimeStampEntryToFileCache(request, response, jarFile)
        }

        sendResource(response,
                if (urlInputStream != null)
                    urlInputStream
                else
                    urlConnection!!.getInputStream())
        return true
    }

    private fun onFile(url: URL, request: Request, response: Response, path: String): Boolean {
        val result: File? = respondedFile(url)
        if (result != null) {
            processFile(request, result, response)
            return true
        } else {
            fine("Resource not found $path")
            return false
        }
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

    private fun lookupResource(resourcePath: String): URL? {
        if (docRoots.isEmpty()) {
            fine("No doc roots registered -> resource $resourcePath is not found ")
            return null
        }

        for (docRoot in docRoots) {
            val docRootPart: String
            when {
                SLASH_STR == docRoot -> docRootPart = EMPTY_STR
                docRoot.startsWith(SLASH_STR) -> docRootPart = docRoot.substring(1)
                else -> docRootPart = docRoot
            }

            val fullPath = docRootPart + resourcePath
            val url = classLoader.getResource(fullPath)

            if (url != null) {
                return url
            }
        }

        return null
    }

    private fun addTimeStampEntryToFileCache(req: Request,
                                             res: Response?,
                                             archive: File): Boolean {
        if (isFileCacheEnabled) {
            val fcContext = req.context
            val fileCacheFilter = lookupFileCache(fcContext)
            if (fileCacheFilter != null) {
                val fileCache = fileCacheFilter.fileCache
                if (fileCache.isEnabled) {
                    if (res != null) {
                        StaticHttpHandlerBase.addCachingHeaders(res, archive)
                    }
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


    internal class JarURLInputStream(private val jarConnection: JarURLConnection,
                                     private val jarFile: JarFile,
                                     src: InputStream) : java.io.FilterInputStream(src) {

        @Throws(IOException::class)
        override fun close() {
            try {
                super.close()
            } finally {
                closeJarFileIfNeeded(jarConnection, jarFile)
            }
        }
    }

    companion object {

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

        private val SLASH_STR = "/"

        private val EMPTY_STR = ""

        @Throws(IOException::class)
        private fun sendResource(response: Response,
                                 input: InputStream) {
            response.setStatus(HttpStatus.OK_200)

            response.addDateHeader(Header.Date, System.currentTimeMillis())
            val chunkSize = 8192

            response.suspend()

            val outputStream = response.nioOutputStream

            outputStream.notifyCanWrite(
                    NonBlockingDownloadHandler(response, outputStream,
                            input, chunkSize))

        }

        @Throws(IOException::class)
        private fun closeJarFileIfNeeded(jarConnection: JarURLConnection,
                                         jarFile: JarFile) {
            if (!jarConnection.useCaches) {
                jarFile.close()
            }
        }
    }
}
