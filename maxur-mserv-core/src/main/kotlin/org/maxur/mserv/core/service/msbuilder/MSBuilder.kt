@file:Suppress("unused")

package org.maxur.mserv.core.service.msbuilder

import org.glassfish.hk2.utilities.Binder
import org.maxur.mserv.core.BaseMicroService
import org.maxur.mserv.core.Locator
import org.maxur.mserv.core.MicroService
import org.maxur.mserv.core.domain.Holder
import org.maxur.mserv.core.embedded.CompositeService
import org.maxur.mserv.core.embedded.EmbeddedService
import org.maxur.mserv.core.embedded.EmbeddedServiceFactory
import org.maxur.mserv.core.service.hk2.LocatorFactoryHK2Impl
import org.maxur.mserv.core.service.properties.PropertiesService
import org.maxur.mserv.core.service.properties.PropertiesServiceFactory
import org.maxur.mserv.core.service.properties.PropertiesSource
import kotlin.reflect.KFunction

abstract class MSBuilder {

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

    protected var propertiesHolder: PropertiesHolder = PropertiesHolder()

    protected var servicesHolder: ServicesHolder = ServicesHolder()
    protected var observersHolder: ObserversHolder? = null

    open fun build(): MicroService {
        val locator = LocatorFactoryHK2Impl {
            this.packages = packagesHolder
            bind(*bindersHolder.toTypedArray())
            bind(propertiesHolder::build, PropertiesService::class.java)
            bind({ locator -> BaseMicroService(servicesHolder.build(locator), locator) }, MicroService::class.java)
        }.make()

        val service = locator.service(MicroService::class.java) ?:
                throw IllegalStateException("A MicroService is not created. Maybe It's configuration is wrong")

        if (service is BaseMicroService) {
            service.name = titleHolder.get(locator)!!
            observersHolder?.let {
                service.beforeStart.addAll(it.beforeStartHolder)
                service.afterStop.addAll(it.afterStopHolder)
                service.onError.addAll(it.onErrorHolder)
            }
        }
        return service
    }
}

open class ServicesHolder {
    protected var serviceHolder: ArrayList<ServiceHolder> = ArrayList()

    fun add(value: ServiceHolder) {
        serviceHolder.add(value)
    }

    fun build(locator: Locator): EmbeddedService = CompositeService(serviceHolder.map { it.build(locator)!! })
}

class ServiceHolder {
    private val clazz = EmbeddedServiceFactory::class.java
    private var holder: Holder<EmbeddedService?> = Holder.none()
    private var propertiesHolder: Holder<Any?> = Holder.wrap(null)

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

    private fun makeServiceHolder(): Holder<EmbeddedService?> {
        return Holder.get {
            locator ->
            locator
                    .locate(typeHolder ?: "unknown", clazz)
                    .make(propertiesHolder)
                    .apply { success() } ?: serviceNotCreatedError()
        }
    }

    var properties: Any? = null
        set(value) {
            this.propertiesHolder = when (value) {
                is String -> propertiesKey(value)
                else -> Holder.wrap(value)
            }
        }

    private fun success() {
        //log.info("Service '$typeHolder' is configured\n")
    }

    private fun serviceNotCreatedError(): Nothing? {
        //log.info("Service '$typeHolder' is not configured\n")
        return null
    }

    private fun propertiesKey(value: String): Holder<Any?> {
        val key: String = if (value.startsWith(":"))
            value.substringAfter(":")
        else
            throw IllegalArgumentException("A Key Name must be started with ':'")
        return Holder.get<Any?> { locator, clazz -> locator.properties(key, clazz)!! }
    }

    fun build(locator: Locator): EmbeddedService? = holder.get<EmbeddedService>(locator)

}

class ObserversHolder {
    var beforeStartHolder: MutableList<KFunction<Any>> = ArrayList()
    var afterStopHolder: MutableList<KFunction<Any>> = ArrayList()
    var onErrorHolder: MutableList<KFunction<Any>> = ArrayList()

    var beforeStart: KFunction<Any>? = null
        set(value) {
            value?.let { beforeStartHolder.add(value) }
        }

    var afterStop: KFunction<Any>? = null
        set(value) {
            value?.let { afterStopHolder.add(value) }
        }

    var onError: KFunction<Any>?  = null
        set(value) {
            value?.let { onErrorHolder.add(value) }
        }
}

class PropertiesHolder {

    companion object {
        private val clazz = PropertiesServiceFactory::class.java
    }

    var format: String = "Hocon"
    var rootKey: String = "DEFAULTS"
    fun none() {
        format = "None"
        rootKey = ""
    }

    fun build(locator: Locator): PropertiesService {

        val source = PropertiesSource(format, rootKey)

        val factory: PropertiesServiceFactory = locator.locate(format, clazz)

        return factory.make(source)
                ?.apply {
                    //log.info("Properties Service is '${factory.name}'\n")
                     }
                ?: throw IllegalStateException("Properties Service '$format' is not configured\n")
    }

}


