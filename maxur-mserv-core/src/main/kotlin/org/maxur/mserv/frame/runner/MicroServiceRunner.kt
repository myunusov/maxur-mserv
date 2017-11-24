package org.maxur.mserv.frame.runner

import com.fasterxml.jackson.databind.ObjectMapper
import org.maxur.mserv.core.CompositeBuilder
import org.maxur.mserv.core.EntityRepository
import org.maxur.mserv.core.LocalEntityRepository
import org.maxur.mserv.core.command.Event
import org.maxur.mserv.frame.BaseMicroService
import org.maxur.mserv.frame.LocatorConfig
import org.maxur.mserv.frame.MicroService
import org.maxur.mserv.frame.domain.Holder
import org.maxur.mserv.frame.embedded.EmbeddedService
import org.maxur.mserv.frame.embedded.EmbeddedServiceFactory
import org.maxur.mserv.frame.embedded.grizzly.WebServerGrizzlyFactoryImpl
import org.maxur.mserv.frame.kotlin.Locator
import org.maxur.mserv.frame.service.EventBus
import org.maxur.mserv.frame.service.hk2.LocatorHK2ImplBuilder
import org.maxur.mserv.frame.service.jackson.ObjectMapperProvider
import org.maxur.mserv.frame.service.properties.Properties
import org.maxur.mserv.frame.service.properties.PropertiesFactory
import org.maxur.mserv.frame.service.properties.PropertiesFactoryHoconImpl
import org.maxur.mserv.frame.service.properties.PropertiesFactoryJsonImpl
import org.maxur.mserv.frame.service.properties.PropertiesFactoryYamlImpl

/**
 * This class is abstract builder of MicroService.
 *
 * it's base class for Java MicroService Builder and Kotlin MicroService Builder.
 *
 * @author Maxim Yunusov
 * @version 1.0
 * @since <pre>11/25/13</pre>
 */
abstract class MicroServiceRunner(
    /** Initialization block. */
    val init: LocatorConfig.() -> Unit = {}) {
    /** List of embedded services.*/
    val services: CompositeBuilder<EmbeddedService> = CompositeServiceBuilder()
    /** List of properties sources.*/
    val properties: CompositePropertiesBuilder = CompositePropertiesBuilder()
    /** List of hooks on after start. */
    val afterStart = Hooks.onService()
    /** List of hooks on before stop.*/
    val beforeStop = Hooks.onService()
    /** List of hooks on errors. */
    val onError = Hooks.onError()
    /** List of project service packages for service locator lookup. */
    var packages = StringsHolder()
    /** Builder of Service Locator instance.*/
    var locatorBuilder: LocatorBuilder = LocatorHK2ImplBuilder()

    protected var nameHolder = Holder.string("Anonymous")

    /** Start Microservice */
    fun start(): List<Event> {
        val service = next()
        return service.start()
    }

    /**
     * Build Microservice.
     * @return new instance of Microservice
     */
    open fun next(): MicroService {
        val locator: Locator = locatorBuilder.apply {
            packages = this@MicroServiceRunner.packages.strings
        }.build {
            bind()
            init()
        }
        val service = locator.service(MicroService::class) ?: locator.onConfigurationError()
        if (service is BaseMicroService) {
            service.name = nameHolder.get(locator)!!
            service.afterStart.addAll(afterStart.list)
            service.beforeStop.addAll(beforeStop.list)
            service.onError.addAll(onError.list)
        }
        return service
    }

    private fun LocatorConfig.bind() {
        bind(this@MicroServiceRunner).to(MicroServiceRunner::class)
        bindFactory(properties::build).to(Properties::class)
        bindFactory(services::build).to(EmbeddedService::class)
        bindFactory({ locator -> BaseMicroService(locator) }).to(MicroService::class)
        bind(ObjectMapperProvider.objectMapper).to(ObjectMapper::class)
        bind(WebServerGrizzlyFactoryImpl::class).to(EmbeddedServiceFactory::class).named("grizzly")
        bind(PropertiesFactoryHoconImpl::class).to(PropertiesFactory::class).named("hocon")
        bind(PropertiesFactoryJsonImpl::class).to(PropertiesFactory::class).named("json")
        bind(PropertiesFactoryYamlImpl::class).to(PropertiesFactory::class).named("yaml")
        bind(LocalEntityRepository::class).to(EntityRepository::class)
        bind(EventBus::class)
    }
}
