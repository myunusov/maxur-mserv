package org.maxur.mserv.core.command

import org.jvnet.hk2.annotations.Service
import org.maxur.mserv.core.EntityRepository
import org.maxur.mserv.core.EventEnvelope
import org.maxur.mserv.frame.kotlin.Locator
import org.maxur.mserv.frame.service.EventBus
import javax.inject.Inject

/** The Command Handler (Base implementation) */
@Service
open class BaseCommandHandler @Inject constructor(
    /** {@inheritDoc} */
    override val locator: Locator,
    private val repository: EntityRepository,
    private val eventBus: EventBus
) : CommandHandler {

    /** {@inheritDoc} */
    override fun handle(command: Command) {
        eventBus.post(
            command.execute()
                .map { EventEnvelope(repository.nextId(it.occurredOn.nano), it) }
                .toList()
        )
    }
}