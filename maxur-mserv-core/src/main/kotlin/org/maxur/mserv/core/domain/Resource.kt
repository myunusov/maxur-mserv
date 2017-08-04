package org.maxur.mserv.core.domain

import java.net.JarURLConnection
import java.net.URI
import java.net.URL
import java.util.jar.JarEntry


data class Resource(val path: String) {

    val url: URL? by lazy {
        this::class.java.getResource(path)?.toURI()?.toURL()
    }

    val subfolder: URI?  by lazy {
        url?.let { findSubfolder(it) }
    }

    private fun findSubfolder(url: URL): URI? {
        val connection = url.openConnection() as JarURLConnection
        val file = connection.jarFile
        try {
            val entries = file.entries()
            while (entries.hasMoreElements()) {
                val jarEntry: JarEntry = entries.nextElement()
                if (isParenOf(jarEntry.name)) {
                    val rootPath = jarEntry.name.substringBeforeLast("/")
                    return URI("classpath:/$rootPath/")
                }
            }
            return null
        } finally {
            file.close()
        }
    }

    private fun isParenOf(path: String) = path.count { it == '/' } == this.path.count { it == '/' }

}