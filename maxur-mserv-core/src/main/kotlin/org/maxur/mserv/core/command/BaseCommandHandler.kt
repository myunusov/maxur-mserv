package org.maxur.mserv.core.command

import org.maxur.mserv.core.EntityRepository
import org.maxur.mserv.frame.kotlin.Locator
import org.maxur.mserv.frame.service.bus.EventBus
import javax.inject.Inject

/** The Command Handler (Base implementation) */
class BaseCommandHandler @Inject constructor(
    /** {@inheritDoc} */
    override val locator: Locator,
    private val repository: EntityRepository,
    private val eventBus: EventBus
) : CommandHandler {

    /** {@inheritDoc} */
    override fun handle(command: Command) {
        eventBus.post(
            command.execute()
                .map {
                    // TODO immutable
                    it.id=repository.nextId(it.occurredOn.nano)
                    it
                }
                .toList()
        )
    }
}