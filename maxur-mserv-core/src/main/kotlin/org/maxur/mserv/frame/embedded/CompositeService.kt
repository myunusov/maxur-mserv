package org.maxur.mserv.frame.embedded

/**
 * The composite embedded service.
 */
class CompositeService(
    /** List of services */
    val services: List<EmbeddedService> = emptyList()) : EmbeddedService {

    /** {@inheritDoc} */
    override fun start() = services.flatMap { it.start() }.toList()

    /** {@inheritDoc} */
    override fun stop() = services.reversed().flatMap { it.stop() }.toList()
}