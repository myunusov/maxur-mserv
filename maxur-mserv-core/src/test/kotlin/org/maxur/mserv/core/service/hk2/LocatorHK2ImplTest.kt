package org.maxur.mserv.core.service.hk2

import org.assertj.core.api.Assertions.assertThat
import org.glassfish.hk2.api.ServiceLocator
import org.glassfish.hk2.api.ServiceLocatorState
import org.glassfish.hk2.utilities.ServiceLocatorUtilities
import org.glassfish.hk2.utilities.binding.AbstractBinder
import org.junit.After
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.maxur.mserv.core.LocatorImpl
import org.maxur.mserv.core.TestLocatorHolder
import org.maxur.mserv.core.service.properties.Properties
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

    private lateinit var serviceLocator: ServiceLocator

    @Before
    fun setUp() {
        serviceLocator = ServiceLocatorUtilities.createAndPopulateServiceLocator("locator-name").also {
            ServiceLocatorUtilities.enableImmediateScope(it)
            ServiceLocatorUtilities.bind(it, Binder())
        }
    }

    inner class Binder : AbstractBinder() {
        override fun configure() {
            bind("A").to(String::class.java).named("A")
            bind("B").to(String::class.java).named("B")
            bind(this@LocatorHK2ImplTest.properties).to(Properties::class.java)
        }
    }

    @After
    fun tearDown() {
        serviceLocator.shutdown()
    }

    @Test
    fun implementation() {
        val locator = LocatorHK2Impl(serviceLocator)
        assertThat(locator.implementation<ServiceLocator>()).isEqualTo(serviceLocator)
    }

    @Test
    fun names() {
        val locator = LocatorHK2Impl(serviceLocator)
        assertThat(locator.names(String::class.java)).isEqualTo(listOf("A", "B"))
    }

    @Test
    fun property() {
            val locator = LocatorHK2Impl(serviceLocator)
            Mockito.`when`(properties.read("A", String::class.java)).thenReturn("A")
            assertThat(locator.property("A", String::class.java)).isEqualTo("A")
    }

    @Test
    fun service() {
        val locator = LocatorHK2Impl(serviceLocator)
        assertThat(locator.service(String::class.java, "A")).isEqualTo("A")
    }

    @Test
    fun services() {
        val locator = LocatorHK2Impl(serviceLocator)
        assertThat(locator.services(String::class.java)).isEqualTo(listOf("A", "B"))
    }

    @Test
    fun close() {
        val locator = LocatorHK2Impl(serviceLocator)
        LocatorImpl.holder.put(locator)
        locator.shutdown()
        assertThat(serviceLocator.state).isEqualTo(ServiceLocatorState.SHUTDOWN)
        locator.shutdown()
    }

}