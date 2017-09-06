package org.maxur.mserv.core.builder

import com.fasterxml.jackson.databind.ObjectMapper
import org.maxur.mserv.core.BaseMicroService
import org.maxur.mserv.core.LocatorConfig
import org.maxur.mserv.core.MicroService
import org.maxur.mserv.core.core.CompositeBuilder
import org.maxur.mserv.core.domain.Holder
import org.maxur.mserv.core.embedded.EmbeddedService
import org.maxur.mserv.core.embedded.EmbeddedServiceFactory
import org.maxur.mserv.core.embedded.grizzly.WebServerGrizzlyFactoryImpl
import org.maxur.mserv.core.kotlin.Locator
import org.maxur.mserv.core.service.hk2.LocatorHK2ImplBuilder
import org.maxur.mserv.core.service.jackson.ObjectMapperProvider
import org.maxur.mserv.core.service.properties.Properties
import org.maxur.mserv.core.service.properties.PropertiesFactory
import org.maxur.mserv.core.service.properties.PropertiesFactoryHoconImpl
import org.maxur.mserv.core.service.properties.PropertiesFactoryJsonImpl
import org.maxur.mserv.core.service.properties.PropertiesFactoryYamlImpl

/**
 * This class is abstract builder of MicroService.
 *
 * it's base class for Java MicroService Builder and Kotlin MicroService Builder.
 *
 * @author Maxim Yunusov
 * @version 1.0
 * @since <pre>11/25/13</pre>
 */
abstract class MicroServiceBuilder(val init: LocatorConfig.() -> Unit = {}) {

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
     */
    var locatorBuilder: LocatorBuilder = LocatorHK2ImplBuilder()

    val locator: Locator by lazy {
        locatorBuilder.apply {
            packages = this@MicroServiceBuilder.packages.strings
        }.build {
            bind()
            init()
        }
    }

    protected var nameHolder = Holder.string("Anonymous")

    /**
     * Build Microservice.
     * @return new instance of Microservice
     */
    open fun build(): MicroService = BaseMicroService(locator).also {
        it.name = nameHolder.get(locator)!!
        it.afterStart.addAll(afterStart.list)
        it.beforeStop.addAll(beforeStop.list)
        it.onError.addAll(onError.list)
        locator.configure {
            bind(it).to(MicroService::class)
        }
    }

    private fun LocatorConfig.bind() {
        bindFactory(properties::build).to(Properties::class)
        bindFactory(services::build).to(EmbeddedService::class)
        bind(ObjectMapperProvider.objectMapper).to(ObjectMapper::class)
        bind(WebServerGrizzlyFactoryImpl::class).to(EmbeddedServiceFactory::class).named("grizzly")
        bind(PropertiesFactoryHoconImpl::class).to(PropertiesFactory::class).named("hocon")
        bind(PropertiesFactoryJsonImpl::class).to(PropertiesFactory::class).named("json")
        bind(PropertiesFactoryYamlImpl::class).to(PropertiesFactory::class).named("yaml")
    }

}
