package org.maxur.mserv.core.core.command

import org.jvnet.hk2.annotations.Contract
import org.maxur.mserv.core.kotlin.Locator
import java.util.concurrent.Executors

@Contract
interface CommandHandler {

    val locator: Locator

    fun handle(command: Command)

    fun withInjector(): CommandHandler = InjectorWrapper(this)

    fun withDelay(millis: Long): CommandHandler = DeferredWrapper(this, millis)

    private class DeferredWrapper(val handler: CommandHandler, val millis: Long) : CommandHandler {
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

    private class InjectorWrapper(val handler: CommandHandler) : CommandHandler {
        /** {@inheritDoc} */
        override val locator: Locator = handler.locator

        /** {@inheritDoc} */
        override fun handle(command: Command) {
            locator.inject(command)
            handler.handle(command)
        }
    }
}