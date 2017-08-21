package org.maxur.mserv.core

import org.assertj.core.api.Assertions.assertThat
import org.junit.BeforeClass
import org.junit.Test
import org.maxur.mserv.core.service.hk2.LocatorFactoryHK2Impl
import kotlin.concurrent.thread
import kotlin.reflect.KParameter
import kotlin.reflect.KType
import kotlin.reflect.defaultType
import kotlin.test.assertFailsWith

class LocatorSpec {

    companion object {
        @JvmStatic
        @BeforeClass
        fun beforeClass() {
            Locator.holder = TestLocatorHolder
        }
    }

    @Test
    fun testSingle() {
        val locator = LocatorFactoryHK2Impl {}.make()
        assertThat(locator).isNotNull()
        locator.shutdown()
    }

    @Test
    fun testSeq() {
        val l1 = LocatorFactoryHK2Impl {}.make()
        assertThat(l1).isNotNull()
        val l2 = LocatorFactoryHK2Impl {}.make()
        assertThat(l2).isNotNull()
        assertThat(l1).isNotEqualTo(l2)
        assertThat(Locator.current).isNotEqualTo(l1)
        assertThat(Locator.current).isEqualTo(l2)
        l1.shutdown()
        l2.shutdown()
    }

    @Volatile
    private var locator1: Locator? = null
    @Volatile
    private var locator2: Locator? = null

    @Test
    fun testParallel() {

        val t1 = thread(
            start = false,
            block = {
                locator1 = LocatorFactoryHK2Impl {}.make()
                assertThat(Locator.current).isEqualTo(locator1)
                while (locator2 == null) {
                }
                assertThat(locator1).isNotEqualTo(locator2)
                assertThat(Locator.current).isNotEqualTo(locator2)
            })

        val t2 = thread(
            start = false,
            block = {
                locator2 = LocatorFactoryHK2Impl {}.make()
                assertThat(Locator.current).isEqualTo(locator2)
                while (locator1 == null) {
                }
                assertThat(locator2).isNotEqualTo(locator1)
                assertThat(Locator.current).isNotEqualTo(locator1)
            })

        var error: Throwable? = null

        t1.uncaughtExceptionHandler = Thread.UncaughtExceptionHandler { _, ex -> error = ex }
        t2.uncaughtExceptionHandler = Thread.UncaughtExceptionHandler { _, ex -> error = ex }

        t1.start()
        t2.start()

        t1.join()
        t2.join()

        locator1?.shutdown()
        locator2?.shutdown()

        if (error != null) {
            throw error as Throwable
        }
    }

    @Test
    fun testNullLocator() {
        val locator = Locator.NullLocator
        assertThat(locator).isNotNull()
        assertThat(locator.name).isEqualToIgnoringCase("null-locator")
        assertFailsWith<IllegalStateException> {
            locator.service(Any::class.java, "")
        }
        assertFailsWith<IllegalStateException> {
            locator.services(Any::class.java)
        }
        assertFailsWith<IllegalStateException> {
            locator.names(Any::class.java)
        }
        assertFailsWith<IllegalStateException> {
            locator.property("", Any::class.java)
        }
        assertFailsWith<IllegalStateException> {
            locator.implementation()
        }
        assertFailsWith<IllegalStateException> {
            locator.shutdown()
        }
    }

    @Test
    fun testName() {
        val locator = FakeLocator
        assertThat(locator).isNotNull()
        assertThat(locator.name).isEqualToIgnoringCase("locator name")
        assertThat(locator.toString()).isEqualToIgnoringCase("locator name")
        locator.shutdown()
    }

    @Test
    fun testHolder() {
        val holder = FakeLocator.holder
        assertThat(holder.get() is Locator.NullLocator)
        assertThat(holder.put(FakeLocator))
        assertThat(holder.get() is FakeLocator)
        assertFailsWith<IllegalStateException> {
            assertThat(holder.put(FakeLocator))
        }
        assertFailsWith<IllegalArgumentException> {
            holder.remove("invalid name")
        }
        holder.remove(FakeLocator.name)
        assertThat(holder.get() is Locator.NullLocator)
    }

    @Test
    fun testCompanionObject() {
        synchronized(this) {
            val locator = FakeLocator
            Locator.current = FakeLocator
            assertThat(Locator.service(Locator::class)).isEqualTo(locator)
            assertThat(Locator.service(Locator::class.java)).isEqualTo(locator)
            assertThat(Locator.service(Locator::class, "")).isEqualTo(locator)
            assertThat(Locator.service(Locator::class.java, "")).isEqualTo(locator)

            assertThat(Locator.services(Locator::class)).isEqualTo(listOf(locator))
            assertThat(Locator.services(Locator::class.java)).isEqualTo(listOf(locator))

            assertThat(Locator.service<Locator>(object : KParameter {
                override val annotations: List<Annotation> = emptyList()
                override val index: Int = 0
                override val isOptional: Boolean = false
                override val isVararg: Boolean = false
                override val kind: KParameter.Kind = KParameter.Kind.VALUE
                override val name: String? = "value"
                override val type: KType = Locator::class.defaultType
            })).isEqualTo(locator)
            Locator.shutdown()
            // Idempotent
            Locator.shutdown()
        }
    }

    @Test
    fun testLocate() {
        val locator = FakeLocator
        assertThat(locator.locate(Locator::class, "")).isEqualTo(locator)
        assertFailsWith<IllegalStateException> {
            assertThat(locator.locate(FakeLocator::class, ""))
        }
    }

    @Test
    fun testNames() {
        val locator = FakeLocator
        assertThat(locator.names(Locator::class)).isEqualTo(listOf(""))
        assertThat(locator.names(FakeLocator::class)).isEqualTo(emptyList<String>())
    }

    @Test
    fun testProperty() {
        val locator = FakeLocator
        assertThat(locator.property("key")).isEqualTo("value")
        assertThat(locator.property("invalidkey")).isNull()
        assertThat(locator.property("key", FakeLocator::class)).isNull()
    }

    @Suppress("UNCHECKED_CAST")
    object FakeLocator : Locator(name = "locator name") {

        override fun <T> service(contractOrImpl: Class<T>, name: String?): T? =
            if (contractOrImpl == Locator::class.java) this as T else null

        override fun <T> services(contractOrImpl: Class<T>): List<T> =
            if (contractOrImpl == Locator::class.java) listOf(this) as List<T> else emptyList()

        override fun names(contractOrImpl: Class<*>): List<String> =
            if (contractOrImpl == Locator::class.java) listOf("") else emptyList()

        override fun <T> property(key: String, clazz: Class<T>): T? =
            if (clazz == String::class.java && key == "key") "value" as T else null

        override fun <T> implementation(): T = Object() as T

        override fun close() = Unit

        var holder: LocatorHolder = SingleHolder()

    }

}
