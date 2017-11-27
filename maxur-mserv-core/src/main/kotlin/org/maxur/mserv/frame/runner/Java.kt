package org.maxur.mserv.frame.runner

import org.maxur.mserv.core.command.Event
import org.maxur.mserv.frame.MicroService
import org.maxur.mserv.frame.domain.BaseService
import org.maxur.mserv.frame.domain.Holder
import java.util.function.Consumer

class Java {
    companion object DSL {
        @JvmStatic
        fun runner(): JRunner = JRunner()
    }
}

interface IJBuilder {
    fun name(value: String): JRunner
    fun packages(value: String): JRunner
    fun properties(format: String): JPropertiesBuilder
    fun properties(): JRunner
    fun properties(key: String, value: Any): JRunner
    fun withoutProperties(): JRunner
    fun service(type: String, properties: String): JRunner
    fun rest(): JRunner
    fun beforeStop(func: Consumer<in BaseService>): JRunner
    fun afterStart(func: Consumer<in BaseService>): JRunner
    fun onError(func: Consumer<Exception>): JRunner
    fun next(): MicroService
    fun start(): List<Event>
}

class JRunner : MicroServiceRunner(), IJBuilder {

    override fun name(value: String): JRunner {
        nameHolder = Holder.string(value)
        return this
    }

    override fun packages(value: String): JRunner {
        packages += value
        return this
    }

    override fun properties(format: String): JPropertiesBuilder {
        val holder = PropertiesBuilder.BasePropertiesBuilder()
        properties += holder
        holder.format = format
        return JPropertiesBuilder(this, holder)
    }

    override fun properties(): JRunner {
        properties += PropertiesBuilder.BasePropertiesBuilder()
        return this
    }

    override fun properties(key: String, value: Any): JRunner {
        properties += Pair(key, value)
        return this
    }

    override fun withoutProperties(): JRunner {
        properties += PropertiesBuilder.NullPropertiesBuilder
        return this
    }

    override fun service(type: String, properties: String): JRunner {
        val holder = ServiceBuilder()
        holder.type = type
        holder.properties = properties
        services.add(holder)
        return this
    }

    override fun rest(): JRunner {
        service("grizzly", ":webapp")
        return this
    }

    override fun beforeStop(func: Consumer<in BaseService>): JRunner {
        beforeStop.plusAssign(unitFunc(func))
        return this
    }

    override fun afterStart(func: Consumer<in BaseService>): JRunner {
        afterStart.plusAssign(unitFunc(func))
        return this
    }

    override fun onError(func: Consumer<Exception>): JRunner {
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
}

class JPropertiesBuilder(
    private val parent: JRunner,
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

