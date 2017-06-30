package org.maxur.mserv.core.embedded

import org.glassfish.hk2.api.ActiveDescriptor
import org.glassfish.hk2.api.Self
import org.jvnet.hk2.annotations.Contract
import org.maxur.mserv.core.domain.Holder
import javax.annotation.PostConstruct
import javax.inject.Inject

/**
 * @author myunusov
 * @version 1.0
 * @since <pre>24.06.2017</pre>
 */
@Contract
abstract class EmbeddedServiceFactory {

    @Inject
    @Self
    private var descriptor: ActiveDescriptor<*>? = null

    lateinit var name: String

    @PostConstruct
    fun init() {
       name = descriptor?.name ?: "Undefined"
    }

    abstract fun make(properties: Holder<Any?>): EmbeddedService?

}