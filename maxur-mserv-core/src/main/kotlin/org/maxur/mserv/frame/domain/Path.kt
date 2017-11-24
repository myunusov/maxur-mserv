package org.maxur.mserv.frame.domain

data class Path(val asString: String) {

    val contextPath: String = asString.replace("/{2,}".toRegex(), "/").trimEnd('/')
}

