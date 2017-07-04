@file:Suppress("unused")

package org.maxur.mserv.core.service.properties

import org.jvnet.hk2.annotations.Service
import java.net.URI

@Service(name = "None")
class PropertiesServiceFactoryNullImpl : PropertiesServiceFactory() {

    override fun make(source: PropertiesSource): PropertiesService? =
            object : PropertiesService {
                override val name: String = "None"
                override fun asString(key: String): String? = error(key)
                override fun asLong(key: String): Long? = error(key)
                override fun asInteger(key: String): Int? = error(key)
                override fun asURI(key: String): URI? = error(key)
                override fun <P> read(key: String, clazz: Class<P>): P? = error(key)
                private fun <T> error(key: String): T =
                        throw IllegalStateException("Service Configuration is not found. Key '$key' unresolved")
            }
}
