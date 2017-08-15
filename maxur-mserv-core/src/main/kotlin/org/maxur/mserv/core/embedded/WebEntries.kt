package org.maxur.mserv.core.embedded

import java.net.URI

class WebEntries(val url: URI) {

    private val entries: MutableList<WebEntry> = ArrayList()

    override fun toString(): String = "\n---Entries---\n" + entries.map { "$url$it" }.joinToString("\n")

    fun add(path: String, pattern: String, startUrl: String) {
        entries.add(WebEntry(path, pattern, startUrl))
    }

    class WebEntry(val path: String, val pattern: String, val startUrl: String) {

        override fun toString(): String {
            return "$path$pattern/$startUrl"
        }
    }

}
