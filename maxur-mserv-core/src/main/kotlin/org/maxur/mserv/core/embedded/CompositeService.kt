package org.maxur.mserv.core.embedded

class CompositeService(val services: List<EmbeddedService>) : EmbeddedService() {
    override fun start() = services.forEach({ it.start() })
    override fun stop() = services.reversed().forEach({ it.stop() })
}