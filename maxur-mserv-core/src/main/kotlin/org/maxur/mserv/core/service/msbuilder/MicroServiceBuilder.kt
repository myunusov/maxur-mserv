@file:Suppress("unused")

package org.maxur.mserv.core.service.msbuilder

import org.glassfish.hk2.utilities.Binder
import org.maxur.mserv.core.BaseMicroService
import org.maxur.mserv.core.Locator
import org.maxur.mserv.core.MicroService
import org.maxur.mserv.core.domain.Holder
import org.maxur.mserv.core.embedded.EmbeddedService
import org.maxur.mserv.core.service.hk2.ErrorHandler
import org.maxur.mserv.core.service.hk2.LocatorFactoryHK2Impl
import org.maxur.mserv.core.service.properties.Properties
import org.maxur.mserv.core.service.properties.PropertiesSource

abstract class MicroServiceBuilder {

    protected var titleHolder: Holder<String> = Holder.string("Anonymous")

    protected var packagesHolder: MutableList<String> = ArrayList()

    protected var bindersHolder: MutableList<Binder> = ArrayList()

    private var binders: Array<Binder> = arrayOf()
        set(value) {
            bindersHolder.addAll(value)
        }
    private var binder: Binder? = null
        set(value) {
            value?.let { bindersHolder.add(value) }
        }

    protected var propertiesHolder: PropertiesHolder = PropertiesHolder.DefaultPropertiesHolder

    val services: CompositeBuilder<EmbeddedService> = CompositeServiceBuilder()
    val afterStart = HookHolder.onService()
    val beforeStop = HookHolder.onService()
    val onError = HookHolder.onError()

    /**
     * Build Microservice.
     */
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
                bind({ locator -> services.build(locator) }, EmbeddedService::class)
                bind({ locator ->
                    BaseMicroService(locator.service<EmbeddedService>(EmbeddedService::class)!!, locator)
                }, MicroService::class)

            }.make()
        } catch (e: Exception) {
            return onConfigurationError(Locator.current)
        }
    }

    private fun buildService(locator: Locator?): MicroService {
        val service = locator?.service(MicroService::class) ?: onConfigurationError(locator)
        if (service is BaseMicroService) {
            service.name = titleHolder.get(locator!!)!!
            service.afterStart.addAll(afterStart.list)
            service.beforeStop.addAll(beforeStop.list)
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

