package org.maxur.mserv.core.builder

import org.maxur.mserv.core.MicroService
import org.maxur.mserv.core.domain.BaseService
import org.maxur.mserv.core.domain.Holder
import java.util.function.Consumer

class Java {
    companion object dsl {
        @JvmStatic
        fun service(): JBuilder = JBuilder()
    }
}

interface IJBuilder {
    fun name(value: String): JBuilder
    fun packages(value: String): JBuilder
    fun properties(format: String): JPropertiesBuilder
    fun properties(): JBuilder
    fun withoutProperties(): JBuilder
    fun service(type: String, properties: String): JBuilder
    fun rest(): JBuilder
    fun beforeStop(func: Consumer<in BaseService>): JBuilder
    fun afterStart(func: Consumer<in BaseService>): JBuilder
    fun onError(func: Consumer<Exception>): JBuilder
    fun build(): MicroService
    fun start()
}

class JBuilder : MicroServiceBuilder(), IJBuilder {

    override fun name(value: String): JBuilder {
        nameHolder = Holder.string(value)
        return this
    }

    override fun packages(value: String): JBuilder {
        packages += value
        return this
    }

    override fun properties(format: String): JPropertiesBuilder {
        val holder = PropertiesBuilder.BasePropertiesBuilder()
        properties += holder
        holder.format = format
        return JPropertiesBuilder(this, holder)
    }

    override fun properties(): JBuilder {
        properties += PropertiesBuilder.BasePropertiesBuilder()
        return this
    }

    override fun withoutProperties(): JBuilder {
        properties += PropertiesBuilder.NullPropertiesBuilder
        return this
    }

    override fun service(type: String, properties: String): JBuilder {
        val holder = ServiceBuilder()
        holder.type = type
        holder.properties = properties
        services.add(holder)
        return this
    }

    override fun rest(): JBuilder {
        service("grizzly", ":webapp")
        return this
    }

    override fun beforeStop(func: Consumer<in BaseService>): JBuilder {
        beforeStop.plusAssign(unitFunc(func))
        return this
    }

    override fun afterStart(func: Consumer<in BaseService>): JBuilder {
        afterStart.plusAssign(unitFunc(func))
        return this
    }

    override fun onError(func: Consumer<Exception>): JBuilder {
        onError.plusAssign(errorFunc(func))
        return this
    }

    private fun unitFunc(func: Consumer<in BaseService>): Function1<BaseService, Unit> =
            object : Function1<BaseService, Unit> {
                override fun invoke(service: BaseService) = func.accept(service)
            }

    private fun errorFunc(func: Consumer<Exception>): Function1<Exception, Unit> =
            object : Function1<Exception, Unit> {
                override fun invoke(e: Exception) = func.accept(e)
            }

    override fun start() {
        build().start()
    }

}

class JPropertiesBuilder(
        private val parent: JBuilder,
        private val builder: PropertiesBuilder.BasePropertiesBuilder
) : IJBuilder by parent {

    fun url(value: String): JPropertiesBuilder {
        builder.url = value
        return this
    }

    fun rootKey(value: String): JPropertiesBuilder {
        builder.rootKey = value
        return this
    }

}

