package org.maxur.mserv.core.core.command

import org.jvnet.hk2.annotations.Service
import org.maxur.mserv.core.kotlin.Locator
import javax.inject.Inject

@Service
open class BaseCommandHandler @Inject constructor(
    /** {@inheritDoc} */
    override val locator: Locator
) : CommandHandler {

    /** {@inheritDoc} */
    override fun handle(command: Command) {
        command.execute()
    }
}