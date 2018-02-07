package org.maxur.mserv.frame.rest

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider
import org.glassfish.jersey.jackson.JacksonFeature
import org.glassfish.jersey.media.multipart.MultiPartFeature
import org.glassfish.jersey.server.ResourceConfig
import org.glassfish.jersey.server.ServerProperties
import org.jvnet.hk2.annotations.Contract
import org.maxur.mserv.frame.service.jackson.ObjectMapperProvider

/**
 * @author myunusov
 * @version 1.0
 * @since <pre>12.06.2017</pre>
 */
@Contract
abstract class RestResourceConfig : ResourceConfig() {

    val packages: MutableList<String> = ArrayList()

    init {
        resources(RestResourceConfig::class.java.`package`.name)
        property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true)
        property(ServerProperties.BV_DISABLE_VALIDATE_ON_EXECUTABLE_OVERRIDE_CHECK, true)
        register(jacksonProvider())
        register(JacksonFeature::class.java)
        register(RuntimeExceptionHandler::class.java)
        register(ServiceEventListener("/"))
        register(MultiPartFeature::class.java)
    }

    final override fun property(name: String, value: Any): ResourceConfig = super.property(name, value)
    final override fun register(component: Any): ResourceConfig = super.register(component)
    final override fun register(componentClass: Class<*>): ResourceConfig = super.register(componentClass)

    /**
     * Adds array of package names which will be used to scan for components
     * @see ResourceConfig.packages(String...)
     * Add package names to packages array for use it for late (ex. swagger)
     * <p/>
     * Package scanning ignores inheritance and therefore {@link Path} annotation
     * on parent classes and interfaces will be ignored.
     * <p/>
     * Packages will be scanned recursively, including all nested packages.
     *
     * @param packages array of package names.
     * @return updated resource configuration instance.

     */
    fun resources(vararg packages: String): ResourceConfig {
        this.packages.addAll(packages)
        return packages(*packages)
    }

    private fun jacksonProvider(): JacksonJaxbJsonProvider {
        val provider = JacksonJaxbJsonProvider()
        provider.setMapper(ObjectMapperProvider.objectMapper)
        return provider
    }

}