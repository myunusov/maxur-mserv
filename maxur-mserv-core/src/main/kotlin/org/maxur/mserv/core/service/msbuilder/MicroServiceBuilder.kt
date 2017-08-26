package org.maxur.mserv.core.service.msbuilder

import org.maxur.mserv.core.BaseMicroService
import org.maxur.mserv.core.Locator
import org.maxur.mserv.core.MicroService
import org.maxur.mserv.core.core.checkError
import org.maxur.mserv.core.domain.Holder
import org.maxur.mserv.core.embedded.EmbeddedService
import org.maxur.mserv.core.service.hk2.LocatorFactoryHK2Impl
import org.maxur.mserv.core.service.properties.Properties
import org.maxur.mserv.core.service.properties.PropertiesSource

/**
 * This class is abstract builder of MicroService.
 *
 * it's base class for Java MicroService Builder and Kotlin MicroService Builder.
 *
 * @author Maxim Yunusov
 * @version 1.0
 * @since <pre>11/25/13</pre>
 */
abstract class MicroServiceBuilder {

    /**
     * List of embedded services.
     */
    val services: CompositeBuilder<EmbeddedService> = CompositeServiceBuilder()

    /**
     * List of hooks on after start.
     */
    val afterStart = Hooks.onService()
    /**
     * List of hooks on before stop.
     */
    val beforeStop = Hooks.onService()
    /**
     * List of hooks on errors.
     */
    val onError = Hooks.onError()

     /**
     * List of project service packages for service locator lookup.
     */
    var packages: StringsHolder = StringsHolder()

    protected var titleHolder: Holder<String> = Holder.string("Anonymous")

    protected var propertiesHolder: PropertiesHolder = PropertiesHolder.DefaultPropertiesHolder

    /**
     * Build Microservice.
     * @return new instance of Microservice
     */
    open fun build(): MicroService = build(
        checkError(
            { buildLocator() },
            { e -> onConfigurationError(Locator.current, e) }
        )
    )

    private fun buildLocator(): Locator = LocatorFactoryHK2Impl {
        this.packages = this@MicroServiceBuilder.packages.strings
        bind(propertiesHolder::build, Properties::class, PropertiesSource::class)
        bind(services::build, EmbeddedService::class)
        bind({ locator -> BaseMicroService(locator) }, MicroService::class)
    }.make()

    private fun build(locator: Locator): MicroService {
        val service = locator.service(MicroService::class) ?: onConfigurationError(locator)
        if (service is BaseMicroService) {
            service.name = titleHolder.get(locator)!!
            service.afterStart.addAll(afterStart.list)
            service.beforeStop.addAll(beforeStop.list)
            service.onError.addAll(onError.list)
        }
        return service
    }

    private fun <T> onConfigurationError(locator: Locator?, error: Exception? = null): T {
        val errorMessage =
            locator?.configurationError()?.message
                ?: error?.message
                ?: "Unknown error"
        locator?.shutdown()
        throw IllegalStateException("A MicroService is not created. $errorMessage")
    }
}

