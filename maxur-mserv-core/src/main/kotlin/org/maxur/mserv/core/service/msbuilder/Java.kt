package org.maxur.mserv.core.service.msbuilder

import org.maxur.mserv.core.MicroService
import org.maxur.mserv.core.domain.Holder
import java.util.function.Consumer


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

    fun rest(): JBuilder {
        val holder = ServiceHolder()
        holder.type = "grizzly"
        holder.properties = ":webapp"
        servicesHolder.add(holder)
        return this
    }

    fun beforeStart(func: Runnable): JBuilder {
        observer.beforeStartHolder.add(unitFunc(func))
        return this
    }
    fun afterStop(func: Runnable): JBuilder {
        observer.afterStopHolder.add(unitFunc(func))
        ObserversHolder().apply {
            afterStop = observer::afterStop
        }
        return this
    }
    fun onError(func: Consumer<Exception>): JBuilder {
        observer.onErrorHolder.add(unitFunc(func))
        ObserversHolder().apply {
            afterStop = observer::afterStop
        }
        return this
    }

    private fun unitFunc(func: Runnable): Function0<Unit> {
        return object : Function0<Unit> {
            override fun invoke() = func.run()
        }
    }

    private fun unitFunc(func: Consumer<Exception>): Function1<Exception, Unit> {
        return object : Function1<Exception, Unit> {
            override fun invoke(e: Exception) = func.accept(e)
        }
    }

    fun start() {
        build().start()
    }


}

