package org.maxur.mserv.core

import org.assertj.core.api.Assertions.assertThat
import org.junit.BeforeClass
import org.junit.Test
import org.maxur.mserv.core.kotlin.Locator
import kotlin.concurrent.thread
import kotlin.reflect.KParameter
import kotlin.reflect.KType
import kotlin.reflect.full.createType
import kotlin.test.assertFailsWith

class LocatorTest {

    companion object {
        @JvmStatic
        @BeforeClass
        fun beforeClass() {
            LocatorImpl.holder = TestLocatorHolder
        }
    }

    @Test
    fun testSingle() {
        val locator = FakeLocator()
        assertThat(locator).isNotNull()
        locator.shutdown()
    }

    @Test
    fun testSeq() {
        val l1 = FakeLocator()
        assertThat(l1).isNotNull()
        val l2 = FakeLocator()
        assertThat(l2).isNotNull()
        assertThat(l1).isNotEqualTo(l2)
        assertThat(Locator.current).isNotEqualTo(l1)
        assertThat(Locator.current).isEqualTo(l2)
        l1.shutdown()
        l2.shutdown()
    }

    @Volatile
    private var locator1: LocatorImpl? = null
    @Volatile
    private var locator2: LocatorImpl? = null

    @Test
    fun testParallel() {

        val t1 = thread(
            start = false,
            block = {
                locator1 = FakeLocator()
                assertThat(Locator.current).isEqualTo(locator1)
                while (locator2 == null) {
                }
                assertThat(locator1).isNotEqualTo(locator2)
                assertThat(Locator.current).isNotEqualTo(locator2)
            })

        val t2 = thread(
            start = false,
            block = {
                locator2 = FakeLocator()
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
        val locator = NullLocator
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
        val locator = FakeLocator()
        assertThat(locator).isNotNull()
        assertThat(locator.name).isEqualToIgnoringCase("locator name")
        assertThat(locator.toString()).isEqualToIgnoringCase("locator name")
        locator.shutdown()
    }

    @Test
    fun testHolder() {
        val locator = FakeLocator()
        val holder = locator.holder
        assertThat(holder.get() is NullLocator)
        assertThat(holder.put(locator))
        assertThat(holder.get() is FakeLocator)
        assertFailsWith<IllegalStateException> {
            assertThat(holder.put(locator))
        }
        assertFailsWith<IllegalArgumentException> {
            holder.remove("invalid name")
        }
        holder.remove(locator.name)
        assertThat(holder.get() is NullLocator)
    }

    @Test
    fun testCompanionObject() {
        synchronized(this) {
            val locator = FakeLocator()
            assertThat(Locator.bean(BaseLocator::class)).isEqualTo(locator)
            assertThat(org.maxur.mserv.core.java.Locator.bean(BaseLocator::class.java)).isEqualTo(locator)
            assertThat(Locator.bean(BaseLocator::class, "")).isEqualTo(locator)
            assertThat(org.maxur.mserv.core.java.Locator.bean(BaseLocator::class.java, "")).isEqualTo(locator)

            assertThat(Locator.beans(BaseLocator::class)).isEqualTo(listOf(locator))
            assertThat(org.maxur.mserv.core.java.Locator.beans(BaseLocator::class.java)).isEqualTo(listOf(locator))

            assertThat(Locator.bean<BaseLocator>(object : KParameter {
                override val annotations: List<Annotation> = emptyList()
                override val index: Int = 0
                override val isOptional: Boolean = false
                override val isVararg: Boolean = false
                override val kind: KParameter.Kind = KParameter.Kind.VALUE
                override val name: String? = "value"
                override val type: KType = BaseLocator::class.createType()
            })).isEqualTo(locator)
            Locator.stop()
            // Idempotent
            Locator.stop()
        }
    }

    @Test
    fun testLocate() {
        val locator = FakeLocator()
        assertThat(locator.service(LocatorImpl::class.java, "")).isEqualTo(locator)
        assertFailsWith<IllegalStateException> {
            assertThat(locator.locate(FakeLocator::class.java, ""))
        }
    }

    @Test
    fun testNames() {
        val locator = FakeLocator()
        assertThat(locator.names(LocatorImpl::class.java)).isEqualTo(listOf(""))
        assertThat(locator.names(LocatorImpl::class.java)).isEqualTo(emptyList<String>())
    }

    @Test
    fun testProperty() {
        val locator = FakeLocator()
        assertThat(locator.property("key", String::class.java)).isEqualTo("value")
        assertThat(locator.property("invalidkey", String::class.java)).isNull()
        assertThat(locator.property("key", FakeLocator::class.java)).isNull()
    }

    @Suppress("UNCHECKED_CAST")
    class FakeLocator(override val name: String = "fake locator") : LocatorImpl {

        override fun configurationError() = null

        override fun <T> service(contractOrImpl: Class<T>, name: String?): T? =
            if (contractOrImpl == BaseLocator::class.java) this as T else null

        override fun <T> services(contractOrImpl: Class<T>): List<T> =
            if (contractOrImpl == BaseLocator::class.java) listOf(this) as List<T> else emptyList()

        override fun names(contractOrImpl: Class<*>): List<String> =
            if (contractOrImpl == BaseLocator::class.java) listOf("") else emptyList()

        override fun <T> property(key: String, clazz: Class<T>): T? =
            if (clazz == String::class.java && key == "key") "value" as T else null

        override fun <T> implementation(): T = Object() as T

        override fun close() = Unit

        var holder: LocatorHolder = SingleHolder()

    }

}
