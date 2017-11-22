package org.maxur.mserv.core.domain

data class Path(val asString: String) {

    val contextPath: String = asString.replace("/{2,}".toRegex(), "/").trimEnd('/')
}

