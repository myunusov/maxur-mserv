package org.maxur.mserv.core.command

import org.jvnet.hk2.annotations.Service
import org.maxur.mserv.frame.kotlin.Locator
import javax.inject.Inject

/** The Command Handler (Base implementation) */
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