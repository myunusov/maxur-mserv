package org.maxur.mserv.frame.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.maxur.mserv.core.CompositeBuilder
import org.maxur.mserv.core.EntityRepository
import org.maxur.mserv.core.LocalEntityRepository
import org.maxur.mserv.core.command.BaseCommandHandler
import org.maxur.mserv.core.command.CommandHandler
import org.maxur.mserv.frame.BaseMicroService
import org.maxur.mserv.frame.LocatorConfig
import org.maxur.mserv.frame.MicroService
import org.maxur.mserv.frame.domain.BaseService
import org.maxur.mserv.frame.domain.Holder
import org.maxur.mserv.frame.embedded.EmbeddedService
import org.maxur.mserv.frame.embedded.EmbeddedServiceFactory
import org.maxur.mserv.frame.embedded.grizzly.WebServerGrizzlyFactoryImpl
import org.maxur.mserv.frame.kotlin.Locator
import org.maxur.mserv.frame.runner.CompositePropertiesBuilder
import org.maxur.mserv.frame.runner.CompositeServiceBuilder
import org.maxur.mserv.frame.runner.Hooks
import org.maxur.mserv.frame.runner.LocatorBuilder
import org.maxur.mserv.frame.runner.StringsHolder
import org.maxur.mserv.frame.service.bus.EventBus
import org.maxur.mserv.frame.service.bus.EventBusGuavaImpl
import org.maxur.mserv.frame.service.jackson.ObjectMapperProvider
import org.maxur.mserv.frame.service.properties.Properties
import org.maxur.mserv.frame.service.properties.PropertiesFactory
import org.maxur.mserv.frame.service.properties.PropertiesFactoryHoconImpl
import org.maxur.mserv.frame.service.properties.PropertiesFactoryJsonImpl
import org.maxur.mserv.frame.service.properties.PropertiesFactoryYamlImpl

interface MicroServiceBuilder {

    /** List of embedded services.*/
    val services: CompositeBuilder<EmbeddedService>
    /** List of properties sources.*/
    val properties: CompositePropertiesBuilder
    /** List of hooks on after start. */
    val afterStart: Hooks<BaseService>
    /** List of hooks on before stop.*/
    val beforeStop: Hooks<BaseService>
    /** List of hooks on errors. */
    val onError: Hooks<Exception>
    /** List of project service packages for service locator lookup. */
    var packages: StringsHolder
    /** Hold source of microservice name **/
    var nameHolder: Holder<String>

    /**
     * Build Microservice.
     * @return new instance of Microservice
     */
    fun build(): MicroService
}

/**
 * Build the microservice.
 *
 * @author Maxim Yunusov
 * @version 1.0
 * @since <pre>11/28/2017</pre>
 */
class MicroServiceBuilderBase(private val locatorBuilder: LocatorBuilder) : MicroServiceBuilder {

    /** {@inheritDoc} */
    override val services: CompositeBuilder<EmbeddedService> = CompositeServiceBuilder()
    /** {@inheritDoc} */
    override val properties: CompositePropertiesBuilder = CompositePropertiesBuilder()
    /** {@inheritDoc} */
    override val afterStart = Hooks.onService()
    /** {@inheritDoc} */
    override val beforeStop = Hooks.onService()
    /** {@inheritDoc} */
    override val onError = Hooks.onError()
    /** {@inheritDoc} */
    override var packages = StringsHolder()
    /** {@inheritDoc} */
    override var nameHolder = Holder.string("Anonymous")

    /** {@inheritDoc} */
    override fun build(): MicroService {
        val locator: Locator = locatorBuilder
            .apply {
                packages = this@MicroServiceBuilderBase.packages.strings
            }
            .build {
                initLocator()
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

    private fun LocatorConfig.initLocator() {
        bind(this@MicroServiceBuilderBase).to(MicroServiceBuilder::class)
        bindFactory(properties::build).to(Properties::class)
        bindFactory(services::build).to(EmbeddedService::class)
        bindFactory({ locator -> BaseMicroService(locator) }).to(MicroService::class)
        bind(ObjectMapperProvider.objectMapper).to(ObjectMapper::class)
        bind(WebServerGrizzlyFactoryImpl::class).to(EmbeddedServiceFactory::class).named("grizzly")
        bind(PropertiesFactoryHoconImpl::class).to(PropertiesFactory::class).named("hocon")
        bind(PropertiesFactoryJsonImpl::class).to(PropertiesFactory::class).named("json")
        bind(PropertiesFactoryYamlImpl::class).to(PropertiesFactory::class).named("yaml")
        bind(LocalEntityRepository::class).to(EntityRepository::class)
        bind(EventBusGuavaImpl::class).to(EventBus::class)
        bind(BaseCommandHandler::class).to(CommandHandler::class)
    }
}