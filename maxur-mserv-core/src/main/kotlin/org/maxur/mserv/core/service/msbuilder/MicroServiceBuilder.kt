@file:Suppress("unused")

package org.maxur.mserv.core.service.msbuilder

import org.glassfish.hk2.utilities.Binder
import org.maxur.mserv.core.BaseMicroService
import org.maxur.mserv.core.Locator
import org.maxur.mserv.core.MicroService
import org.maxur.mserv.core.domain.BaseService
import org.maxur.mserv.core.domain.Holder
import org.maxur.mserv.core.embedded.CompositeService
import org.maxur.mserv.core.embedded.EmbeddedService
import org.maxur.mserv.core.embedded.EmbeddedServiceFactory
import org.maxur.mserv.core.service.hk2.ErrorHandler
import org.maxur.mserv.core.service.hk2.LocatorFactoryHK2Impl
import org.maxur.mserv.core.service.properties.Properties
import org.maxur.mserv.core.service.properties.PropertiesSource
import java.net.URI
import kotlin.reflect.KFunction

/**
 * @todo Implement support of default options for best service choice
 */
abstract class MicroServiceBuilder {

    protected var titleHolder: Holder<String> = Holder.string("Anonymous")

    protected var packagesHolder: MutableList<String> = ArrayList()

    protected var bindersHolder: MutableList<Binder> = ArrayList()

    var binders: Array<Binder> = arrayOf()
        set(value) {
            bindersHolder.addAll(value)
        }
    var binder: Binder? = null
        set(value) {
            value?.let { bindersHolder.add(value) }
        }

    var propertiesHolder: PropertiesHolder = PropertiesHolder.DefaultPropertiesHolder

    val services: ServicesHolder = ServicesHolder()
    val beforeStart = HookHolder()
    val afterStart = HookHolder()
    val afterStop = HookHolder()
    val beforeStop = HookHolder()
    val onError = ErrorHookHolder()

    open fun build(): MicroService {
        val locator = buildLocator()
        return buildService(locator)
    }

    private fun buildLocator(): Locator? {
        try {
            return LocatorFactoryHK2Impl {
                this.packages = packagesHolder
                bind(*bindersHolder.toTypedArray())
                bind(propertiesHolder::build, Properties::class, PropertiesSource::class)
                bind({ locator -> BaseMicroService(services.build(locator), locator) }, MicroService::class)
            }.make()
        } catch (e: Exception) {
            return onConfigurationError(Locator.current)
        }
    }

    private fun buildService(locator: Locator?): MicroService {
        val service = locator?.service(MicroService::class) ?: onConfigurationError(locator)
        if (service is BaseMicroService) {
            service.name = titleHolder.get(locator!!)!!
            service.beforeStart.addAll(beforeStart.list)
            service.afterStart.addAll(afterStart.list)
            service.beforeStop.addAll(beforeStop.list)
            service.afterStop.addAll(afterStop.list)
            service.onError.addAll(onError.list)
        }
        return service
    }

    private fun <T> onConfigurationError(locator: Locator?): T {
        val errorMessage = locator
                ?.service(ErrorHandler::class)
                ?.latestError
                ?.message
                ?: "Unknown error"
        Locator.current.shutdown()
        throw IllegalStateException("A MicroService is not created. $errorMessage")
    }
}

open class ServicesHolder {
    protected var list: ArrayList<ServiceHolder> = ArrayList()

    fun add(value: ServiceHolder) {
        list.add(value)
    }

    operator fun plusAssign(value: ServiceHolder) {
        list.add(value)
    }

    operator fun plusAssign(value: EmbeddedService) {
        val holder = ServiceHolder()
        holder.ref = value
        list.add(holder)
    }

    fun build(locator: Locator): EmbeddedService = CompositeService(list.map { it.build(locator)!! })
}

class ServiceHolder {
    private val clazz = EmbeddedServiceFactory::class
    private var holder: Holder<EmbeddedService> = Holder.none()
    private var propertiesHolder: Holder<Any> = Holder.wrap(null)
    val beforeStart = HookHolder()
    val afterStart = HookHolder()
    var afterStop = HookHolder()
    var beforeStop = HookHolder()

    var ref: EmbeddedService? = null
        set(value) {
            this.propertiesHolder = Holder.none()
            this.holder = Holder.wrap(value)
        }

    var typeHolder: String? = null

    var type: String?
        get() = typeHolder
        set(value) {
            this.typeHolder = value
            this.holder = makeServiceHolder()
        }

    var properties: Any? = null
        set(value) {
            this.propertiesHolder = when (value) {
                is String -> propertiesKey(value)
                else -> Holder.wrap(value)
            }
            this.holder = makeServiceHolder()
        }

    private fun makeServiceHolder(): Holder<EmbeddedService> {
        return Holder.get { locator ->
            locator
                    .locate(clazz, typeHolder ?: "unknown")
                    .make(propertiesHolder) ?:
                    throw IllegalStateException("Service '$typeHolder' is not configured\n")
        }
    }

    private fun propertiesKey(value: String): Holder<Any> {
        val key: String = if (value.startsWith(":"))
            value.substringAfter(":")
        else
            throw IllegalArgumentException("A Key Name must be started with ':'")
        return Holder.get<Any> { locator, clazz -> locator.property(key, clazz)!! }
    }

    fun build(locator: Locator): EmbeddedService? {
        val service = holder.get<EmbeddedService>(locator)
        if (service is BaseService) {
            service.beforeStart.addAll(beforeStart.list)
            service.afterStart.addAll(afterStart.list)
            service.beforeStop.addAll(beforeStop.list)
            service.afterStop.addAll(afterStop.list)
        }
        return service
    }

}

sealed class PropertiesHolder {

    abstract fun build(locator: Locator): Properties

    class BasePropertiesHolder : PropertiesHolder() {
        lateinit var format: String
        var uri: URI? = null
        var rootKey: String? = null
        var url: String = ""
            set(value) {
                uri = URI.create(value)
            }

        override fun build(locator: Locator): Properties = PropertiesSource.open(format, uri, rootKey)
    }

    object NullPropertiesHolder : PropertiesHolder() {
        override fun build(locator: Locator): Properties = PropertiesSource.nothing()
    }

    object DefaultPropertiesHolder : PropertiesHolder() {
        override fun build(locator: Locator): Properties = PropertiesSource.default()
    }
}

class HookHolder {

    val list: MutableList<KFunction<Any>> = ArrayList()

    operator fun plusAssign(value: KFunction<Any>) {
        list.add(value)
    }

    operator fun plusAssign(value: (service: BaseService) -> Unit) {
        val observer = object {
            fun invoke(service: BaseService) = value.invoke(service)
        }
        list.add(observer::invoke)
    }
}

class ErrorHookHolder {

    val list: MutableList<KFunction<Any>> = ArrayList()

    operator fun plusAssign(value: KFunction<Any>) {
        list.add(value)
    }

    operator fun plusAssign(value: (Exception) -> Unit) {
        val observer = object {
            fun invoke(e: Exception) = value.invoke(e)
        }
        list.add(observer::invoke)
    }
}
