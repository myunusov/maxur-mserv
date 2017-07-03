package org.maxur.mserv.core.service.msbuilder

import org.maxur.mserv.core.MicroService
import org.maxur.mserv.core.domain.Holder


class Java {

    companion object dsl {
        fun service(): JBuilder {
            try {
                return JBuilder()
            } catch (e: Exception) {
                return object: JBuilder() {
                    override fun build(): MicroService = NullService()
                }
            }
        }
    }
}

open class JBuilder: MSBuilder() {

    fun title(value: String): JBuilder {
        titleHolder = Holder.string(value)
        return this
    }

    fun packages(value: String): JBuilder {
        packagesHolder.addAll(value.split("\\s*,\\s*"))
        return this
    }

    fun properties(format: String, root: String): JBuilder {
        propertiesHolder = PropertiesHolder()
        propertiesHolder.format = format
        propertiesHolder.rootKey = root
        return this
    }

    fun properties(format: String): JBuilder {
        propertiesHolder = PropertiesHolder()
        propertiesHolder.format = format
        propertiesHolder.rootKey = "DEFAULTS"
        return this
    }

    fun properties(): JBuilder {
        propertiesHolder = PropertiesHolder()
        propertiesHolder.format = "hocon"
        propertiesHolder.rootKey = "DEFAULTS"
        return this
    }
    
    fun service(type: String, properties: String): JBuilder {
        val holder = ServiceHolder()
        holder.type = type
        holder.properties = properties
        servicesHolder.add(holder)
        return this
    }



}

