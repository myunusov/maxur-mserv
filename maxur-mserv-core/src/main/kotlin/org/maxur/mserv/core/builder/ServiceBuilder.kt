package org.maxur.mserv.core.builder

import org.maxur.mserv.core.core.Builder
import org.maxur.mserv.core.core.CompositeBuilder
import org.maxur.mserv.core.domain.BaseService
import org.maxur.mserv.core.domain.Holder
import org.maxur.mserv.core.embedded.CompositeService
import org.maxur.mserv.core.embedded.EmbeddedService
import org.maxur.mserv.core.embedded.EmbeddedServiceFactory
import org.maxur.mserv.core.kotlin.Locator

/**
 * The Service Builder.
 */
class ServiceBuilder : Builder<EmbeddedService?> {

    val afterStart = Hooks.onService()
    val beforeStop = Hooks.onService()

    private var holder: Holder<EmbeddedService> = Holder.none()
    private var propertiesHolder: Holder<Any> = Holder.wrap(null)

    var ref: EmbeddedService? = null
        set(value) {
            this.propertiesHolder = Holder.none()
            this.holder = Holder.wrap(value)
        }

    var typeHolder: String? = null

    var type: String?
        get() = typeHolder
        set(value) {
            this.typeHolder = value
            this.holder = makeServiceHolder()
        }

    var properties: Any? = null
        set(value) {
            this.propertiesHolder = when (value) {
                is String -> propertiesKey(value)
                else -> Holder.wrap(value)
            }
            this.holder = makeServiceHolder()
        }

    private fun makeServiceHolder(): Holder<EmbeddedService> {
        return Holder.get { locator ->
            locator
                    .locate(EmbeddedServiceFactory::class, typeHolder?.toLowerCase() ?: "unknown")
                    .make(propertiesHolder) ?:
                    throw IllegalStateException("Service '$typeHolder' is not configured\n")
        }
    }

    private fun propertiesKey(value: String): Holder<Any> {
        val key: String = if (value.startsWith(":"))
            value.substringAfter(":")
        else
            throw IllegalArgumentException("A Key Name must be started with ':'")
        return Holder.get { locator, clazz -> locator.property(key, clazz)!! }
    }

    override fun build(locator: Locator): EmbeddedService? {
        val service = holder.get<EmbeddedService>(locator)
        if (service is BaseService) {
            service.afterStart.addAll(afterStart.list)
            service.beforeStop.addAll(beforeStop.list)
        }
        return service
    }
}

/**
 * Build composite service by Embedded Services
 */
internal class CompositeServiceBuilder : CompositeBuilder<EmbeddedService>() {

    /**
     * Wrap new service to service holder and add it to holders list.
     * @param value The Embedded Service.
     */
    operator fun plusAssign(value: EmbeddedService) {
        this += ServiceBuilder().apply { ref = value }
    }

    /** {@inheritDoc} */
    override fun build(locator: Locator): EmbeddedService = buildListWith(locator).let {
        when (it.size) {
            1 -> it[0]
            else -> CompositeService(it)
        }
    }

}