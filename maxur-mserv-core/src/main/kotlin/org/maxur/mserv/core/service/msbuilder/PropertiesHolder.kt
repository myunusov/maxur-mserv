package org.maxur.mserv.core.service.msbuilder

import org.maxur.mserv.core.Locator
import org.maxur.mserv.core.service.properties.Properties
import org.maxur.mserv.core.service.properties.PropertiesSource
import java.net.URI

sealed class PropertiesHolder {

    abstract fun build(locator: Locator): Properties

    class BasePropertiesHolder : PropertiesHolder() {
        lateinit var format: String
        var uri: URI? = null
        var rootKey: String? = null
        var url: String = ""
            set(value) {
                uri = URI.create(value)
            }

        override fun build(locator: Locator): Properties = PropertiesSource.open(format, uri, rootKey)
    }

    object NullPropertiesHolder : PropertiesHolder() {
        override fun build(locator: Locator): Properties = PropertiesSource.nothing()
    }

    object DefaultPropertiesHolder : PropertiesHolder() {
        override fun build(locator: Locator): Properties = PropertiesSource.default()
    }
}