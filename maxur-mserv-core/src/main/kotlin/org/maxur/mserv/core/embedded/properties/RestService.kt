package org.maxur.mserv.core.embedded.properties

import com.fasterxml.jackson.annotation.JsonProperty
import org.maxur.mserv.core.domain.Path

data class RestService(
        @JsonProperty("name", required = false) val name: String?,
        @JsonProperty("path") val path: Path
)

