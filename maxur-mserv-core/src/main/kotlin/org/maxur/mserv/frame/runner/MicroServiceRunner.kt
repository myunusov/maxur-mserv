package org.maxur.mserv.frame.runner

import org.maxur.mserv.core.command.CommandHandler
import org.maxur.mserv.frame.command.ServiceCommand
import org.maxur.mserv.frame.kotlin.Locator
import org.maxur.mserv.frame.service.MicroServiceBuilder
import org.maxur.mserv.frame.service.MicroServiceBuilderBase
import org.maxur.mserv.frame.service.hk2.LocatorHK2ImplBuilder

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
    /** Builder of Service Locator instance.*/
    private val locatorBuilder: LocatorBuilder = LocatorHK2ImplBuilder(),
    private val builder: MicroServiceBuilder = MicroServiceBuilderBase(locatorBuilder)
): MicroServiceBuilder by builder {

    /** Start Microservice */
    fun start() {
        val service = builder.build()
        val start = ServiceCommand.Start(service)
        val handler = Locator.bean(CommandHandler::class)
        handler?.handle(start) ?: throw IllegalStateException("Command Handler is not found")
    }
}
