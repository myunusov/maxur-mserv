package org.maxur.mserv.core.service.msbuilder

import org.maxur.mserv.core.MicroService
import org.maxur.mserv.core.domain.BaseService
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
        services.add(holder)
        return this
    }

    fun rest(): JBuilder {
        service( "grizzly", ":webapp")
        return this
    }

    fun beforeStart(func: Consumer<in BaseService>): JBuilder {
        beforeStart.plusAssign(unitFunc(func))
        return this
    }
    fun afterStop(func: Consumer<in BaseService>): JBuilder {
        afterStop.plusAssign(unitFunc(func))
        return this
    }
    fun beforeStop(func: Consumer<in BaseService>): JBuilder {
        beforeStop.plusAssign(unitFunc(func))
        return this
    }
    fun afterStart(func: Consumer<in BaseService>): JBuilder {
        afterStart.plusAssign(unitFunc(func))
        return this
    }
    fun onError(func: Consumer<Exception>): JBuilder {
        onError.plusAssign(errorFunc(func))
        return this
    }

    private fun unitFunc(func: Consumer<in BaseService>): Function1<BaseService, Unit> {
        return object : Function1<BaseService, Unit> {
            override fun invoke(service: BaseService) = func.accept(service)
        }
    }

    private fun errorFunc(func: Consumer<Exception>): Function1<Exception, Unit> {
        return object : Function1<Exception, Unit> {
            override fun invoke(e: Exception) = func.accept(e)
        }
    }

    fun start() {
        build().start()
    }


}

