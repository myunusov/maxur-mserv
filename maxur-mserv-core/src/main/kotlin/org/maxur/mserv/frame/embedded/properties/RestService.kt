package org.maxur.mserv.frame.embedded.properties

import com.fasterxml.jackson.annotation.JsonProperty
import org.maxur.mserv.frame.domain.Path

data class RestService(
        @JsonProperty("name", required = false) val name: String?,
        @JsonProperty("path") val path: Path
)

