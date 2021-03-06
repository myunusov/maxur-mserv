package org.maxur.mserv.core.command

import org.maxur.mserv.frame.kotlin.Locator
import java.util.concurrent.Executors

/** The Command Handler */
interface CommandHandler {

    /** The Service Locator */
    val locator: Locator

    /** Handle the [command] */
    fun handle(command: Command)

    /** Wrap the command handler with IoC injector */
    fun withInjector(): CommandHandler = InjectorWrapper(this)

    /** Wrap the command handler with delayed service. The [millis] is the length of time to delay in milliseconds */
    fun withDelay(millis: Long): CommandHandler = DeferredWrapper(this, millis)

    private class DeferredWrapper(
        /** The Wrapped command handler */
        val handler: CommandHandler,
        /** The length of time to delay in milliseconds */
        val millis: Long
    ) : CommandHandler {
        /** {@inheritDoc} */
        override val locator: Locator = handler.locator

        /** {@inheritDoc} */
        override fun handle(command: Command) {
            locator.inject(command)
            postpone({ handler.handle(command) })
        }

        private fun postpone(func: () -> Unit) {
            val pool = Executors.newSingleThreadExecutor { runnable ->
                val thread = Executors.defaultThreadFactory().newThread(runnable)
                thread.isDaemon = false
                thread
            }
            pool.submit {
                Thread.sleep(millis)
                func.invoke()
            }
            pool.shutdown()
        }
    }

    private class InjectorWrapper(
        /** The Wrapped command handler */
        val handler: CommandHandler) : CommandHandler {
        /** {@inheritDoc} */
        override val locator: Locator = handler.locator

        /** {@inheritDoc} */
        override fun handle(command: Command) {
            locator.inject(command)
            handler.handle(command)
        }
    }
}