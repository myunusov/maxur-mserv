package org.maxur.mserv.frame.sample

import org.jvnet.hk2.annotations.Service
import org.maxur.mserv.frame.annotation.Value
import org.maxur.mserv.frame.embedded.properties.WebAppProperties
import javax.inject.Inject

@Service
class SampleService @Inject constructor(@Value(key = "webapp") val webapp: WebAppProperties) {

    @Value(key = "name")
    lateinit var name: String

    @Inject
    fun setNewName(@Value(key = "name") value: String) {
        name = value
    }
}

