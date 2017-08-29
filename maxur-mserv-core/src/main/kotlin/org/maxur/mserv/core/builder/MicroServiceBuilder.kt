package org.maxur.mserv.core.builder

import org.maxur.mserv.core.BaseMicroService
import org.maxur.mserv.core.MicroService
import org.maxur.mserv.core.domain.Holder
import org.maxur.mserv.core.embedded.EmbeddedService
import org.maxur.mserv.core.kotlin.Locator
import org.maxur.mserv.core.service.hk2.LocatorHK2ImplBuilder
import org.maxur.mserv.core.service.properties.Properties

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
     * List of properties sources.
     */
    val properties: CompositeBuilder<Properties> = CompositePropertiesBuilder()
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
    var packages = StringsHolder()

    /**
     * Builder of Service Locator instance.
     * XXX Remove implementation by DIP
     */
    var locatorBuilder: LocatorBuilder = LocatorHK2ImplBuilder {
        bind(properties::build).to(Properties::class)
        bind(services::build).to(EmbeddedService::class)
        bind({ locator -> BaseMicroService(locator) }).to(MicroService::class)
    }

    protected var nameHolder = Holder.string("Anonymous")

    /**
     * Build Microservice.
     * @return new instance of Microservice
     */
    open fun build(): MicroService = build(
            locatorBuilder.apply {
                packages = this@MicroServiceBuilder.packages.strings
            }.build()
    )

    private fun build(locator: Locator): MicroService {
        val service = locator.service(MicroService::class) ?: locator.onConfigurationError()
        if (service is BaseMicroService) {
            service.name = nameHolder.get(locator)!!
            service.afterStart.addAll(afterStart.list)
            service.beforeStop.addAll(beforeStop.list)
            service.onError.addAll(onError.list)
        }
        return service
    }
}
