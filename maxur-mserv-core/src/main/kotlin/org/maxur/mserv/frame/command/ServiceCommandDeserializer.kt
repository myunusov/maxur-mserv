package org.maxur.mserv.frame.command

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode

/** ServiceCommandDeserializer */
class ServiceCommandDeserializer : JsonDeserializer<ServiceCommand>() {

    /** {@inheritDoc} */
    override fun deserialize(parser: JsonParser, context: DeserializationContext): ServiceCommand {
        val type = parser.codec.readTree<JsonNode>(parser).get("type")?.asText()
        if (type == null) {
            throw IllegalArgumentException("Command type is not found")
        }
        return when (type.toUpperCase()) {
            "STOP" -> ServiceCommand.stop()
            "RESTART" -> ServiceCommand.restart()
            else -> throw IllegalArgumentException("Command '$type' unknown")
        }
    }
}