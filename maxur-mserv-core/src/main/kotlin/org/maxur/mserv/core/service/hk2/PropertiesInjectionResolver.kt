package org.maxur.mserv.core.service.hk2

import org.glassfish.hk2.api.Injectee
import org.glassfish.hk2.api.InjectionResolver
import org.glassfish.hk2.api.ServiceHandle
import org.maxur.mserv.core.annotation.Value
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
class PropertiesInjectionResolver @Inject constructor(val service: Properties) : InjectionResolver<Value> {

    companion object {
        val log: Logger = LoggerFactory.getLogger(PropertiesInjectionResolver::class.java)
    }

    override fun resolve(injectee: Injectee, root: ServiceHandle<*>?): Any {
        val annotation = annotation(injectee)
        val name = annotation.key
        val result = resolveByKey(name, injectee.requiredType)
        when (result) {
            null -> throw IllegalStateException("Property '$name' is not found")
            else -> return result
        }
    }

    private fun annotation(injectee: Injectee): Value {
        val element = injectee.parent

        val isConstructor = element is Constructor<*>
        val isMethod = element is Method

        // if injectee is method or constructor, check its parameters
        if (isConstructor || isMethod) {
            val annotations: Array<Annotation>
            if (isMethod) {
                annotations = (element as Method).parameterAnnotations[injectee.position]
            } else {
                annotations = (element as Constructor<*>).parameterAnnotations[injectee.position]
            }
            annotations.filterIsInstance<Value>().forEach { return it }
        }

        // check injectee itself (method, constructor or field)
        if (element.isAnnotationPresent(Value::class.java)) {
            return element.getAnnotation(Value::class.java)
        }

        // check class which contains injectee
        val clazz = injectee.injecteeClass
        return clazz.getAnnotation(Value::class.java)
    }


    private fun resolveByKey(name: String, type: Type): Any? {
        if (type !is Class<*>) {
            val msg = "Unsupported property type '${type.typeName}'"
            log.error(msg)
            throw IllegalStateException(msg)
        }
        return service.read(name, type)
    }

    override fun isConstructorParameterIndicator(): Boolean {
        return true
    }

    override fun isMethodParameterIndicator(): Boolean {
        return false
    }
}
