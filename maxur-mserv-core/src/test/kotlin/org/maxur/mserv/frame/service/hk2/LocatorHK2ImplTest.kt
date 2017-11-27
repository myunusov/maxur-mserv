package org.maxur.mserv.frame.service.hk2

import org.assertj.core.api.Assertions.assertThat
import org.glassfish.hk2.api.ServiceLocator
import org.junit.After
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.maxur.mserv.frame.LocatorImpl
import org.maxur.mserv.frame.TestLocatorHolder
import org.maxur.mserv.frame.service.properties.Properties
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LocatorHK2ImplTest {

    companion object {
        @JvmStatic
        @BeforeClass
        fun beforeClass() {
            LocatorImpl.holder = TestLocatorHolder
        }
    }

    @Mock
    private lateinit var properties: Properties

    private lateinit var locator: LocatorImpl

    @Before
    fun setUp() {
        locator = LocatorHK2Impl("locator-name")
        locator.configure {
            bind("A").named("a")
            bind("B").named("b")
            bind(this@LocatorHK2ImplTest.properties).to(Properties::class)
        }
    }

    @After
    fun tearDown() {
        locator.shutdown()
    }

    @Test
    fun implementation() {
        val locator = LocatorHK2Impl("locator-name")
        assertThat(locator.implementation<ServiceLocator>()).isNotNull()
    }

    @Test
    fun names() {
        assertThat(locator.names(String::class.java)).isEqualTo(listOf("a", "b"))
    }

    @Test
    fun property() {
        Mockito.`when`(properties.read("A", String::class.java)).thenReturn("A")
        assertThat(locator.property("A", String::class.java)).isEqualTo("A")
    }

    @Test
    fun service() {
        assertThat(locator.service(String::class.java, "a")).isEqualTo("A")
    }

    @Test
    fun services() {
        assertThat(locator.services(String::class.java)).isEqualTo(listOf("A", "B"))
    }

    @Test
    fun close() {
        LocatorImpl.holder.put(locator)
        locator.shutdown()
        // idempotent
        locator.shutdown()
    }
}