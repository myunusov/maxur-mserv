package org.maxur.mserv.core.embedded.properties

import com.fasterxml.jackson.annotation.JsonProperty

data class RestService(
        @JsonProperty("name", required = false) val name: String?,
        @JsonProperty("path") val path: Path
)

