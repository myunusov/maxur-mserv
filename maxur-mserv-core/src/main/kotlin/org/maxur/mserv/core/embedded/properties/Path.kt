package org.maxur.mserv.core.embedded.properties

data class Path(val path: String) {

    val contextPath: String = run {
        // Map the path to the processor.
        val ex = path.replace("/{2,}".toRegex(), "/")
        if (ex.endsWith("/")) ex.substring(0, ex.length - 1) else ex
    }
    
}

