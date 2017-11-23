package org.maxur.mserv.core.embedded

import org.glassfish.jersey.server.ResourceConfig
import org.maxur.mserv.core.domain.Path
import org.maxur.mserv.core.embedded.properties.StaticContent
import java.net.URI

data class WebAppConfig(
    val url: URI,
    val restPath: Path,
    val staticContent: List<StaticContent>,
    val resourceConfig: ResourceConfig
) {

    // TODO condition must be rewrite
    fun staticContentByPath(path: String): StaticContent? =
        staticContent.filter { it.path.asString == path || "/${it.path.asString}" == path }.firstOrNull()
}

