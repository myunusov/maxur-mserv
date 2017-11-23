package org.maxur.mserv.core.embedded

/**
 * The composite embedded service.
 */
class CompositeService(val services: List<EmbeddedService> = emptyList()) : EmbeddedService {

    /** {@inheritDoc} */
    override fun start() = services.forEach({ it.start() })

    /** {@inheritDoc} */
    override fun stop() = services.reversed().forEach({ it.stop() })
}