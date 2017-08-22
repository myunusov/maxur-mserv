package org.maxur.mserv.core.service.hk2

import org.glassfish.hk2.api.Injectee
import org.glassfish.hk2.api.InjectionResolver
import org.glassfish.hk2.api.ServiceHandle
import org.maxur.mserv.core.Locator
import org.maxur.mserv.core.annotation.Value
import org.maxur.mserv.core.service.properties.NullProperties
import org.maxur.mserv.core.service.properties.Properties
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.reflect.Constructor
import java.lang.reflect.Method
import java.lang.reflect.Type
import javax.inject.Inject

/**
 * Resolve configuration by ConfigParameter annotations.
 *
 * @author myunusov
 * @version 1.0
 * @since <pre>12.06.2017</pre>
 */
class PropertiesInjectionResolver @Inject constructor(val locator: Locator) : InjectionResolver<Value> {

    companion object {
        val log: Logger = LoggerFactory.getLogger(PropertiesInjectionResolver::class.java)
    }

    val service: Properties = locator.service(Properties::class) ?: NullProperties

    override fun resolve(injectee: Injectee, root: ServiceHandle<*>?) =
            resolveByKey(annotation(injectee).key, injectee.requiredType) ?:
                    throw IllegalStateException("Property '${annotation(injectee).key}' is not found")

    private fun annotation(injectee: Injectee): Value {
        val element = injectee.parent
        return when {
            element is Method -> element.parameterAnnotations[injectee.position]
                    .filterIsInstance<Value>()
                    .first()
            element is Constructor<*> -> element.parameterAnnotations[injectee.position]
                    .filterIsInstance<Value>()
                    .first()
            else -> {
                if (element.isAnnotationPresent(Value::class.java)) {
                    element.getAnnotation(Value::class.java)
                } else {
                    injectee.injecteeClass.getAnnotation(Value::class.java)
                }
            }
        }
    }

    private fun resolveByKey(name: String, type: Type) =
            when (type) {
                is Class<*> -> service.read(name, type)
                else -> "Unsupported property type '${type.typeName}'"
                        .also {
                            log.error(it)
                            throw IllegalStateException(it)
                        }
            }

    override fun isConstructorParameterIndicator(): Boolean {
        return true
    }

    override fun isMethodParameterIndicator(): Boolean {
        return true
    }
}
