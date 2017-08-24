package org.maxur.mserv.core

import java.io.File
import java.net.URI
import java.net.URL

/**
 * Extract relative path by resource file name
 * @param name Name of file
 * @return relative path
 */
fun relativePathByResourceName(name: String): String? {
    return ClassLoaderHolder.resource(name)?.relativePath()
}

/**
 * Extract relative path by file
 * @return relative path
 */
fun URL.relativePath(): String {
    return this.toURI().relativePath()
}

/**
 * Extract relative path by uri
 * @return relative path
 */
fun URI.relativePath(): String {
    val base = System.getProperty("user.dir").replace('\\', '/')
    return File(base).toURI().relativize(this).getPath()
}

private object ClassLoaderHolder {

    /**
     * Return Resource by it's name
     * @param name name of the desired resource
     *
     * @return      A  {@link java.net.URL} object or {@code null} if no
     *              resource with this name is found
     */
    fun resource(name: String): URL? {
        return this::class.java.getResource(name)
    }
}
