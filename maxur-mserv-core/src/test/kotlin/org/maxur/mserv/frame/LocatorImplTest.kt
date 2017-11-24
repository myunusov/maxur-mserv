package org.maxur.mserv.frame

import org.assertj.core.api.Assertions.assertThat
import org.junit.BeforeClass
import org.junit.Test
import org.maxur.mserv.frame.kotlin.Locator
import org.maxur.mserv.frame.runner.LocatorBuilder
import kotlin.concurrent.thread
import kotlin.reflect.KParameter
import kotlin.reflect.KType
import kotlin.reflect.full.createType
import kotlin.test.assertFailsWith

class LocatorImplTest {

    companion object {
        @JvmStatic
        @BeforeClass
        fun beforeClass() {
            LocatorImpl.holder = TestLocatorHolder
        }
    }

    @Test
    fun testSingle() {
        val locator = FakeBuilder.build({})
        assertThat(locator).isNotNull()
        locator.shutdown()
    }

    @Test
    fun testSeq() {
        val l1 = FakeBuilder.build({})
        assertThat(l1).isNotNull()
        val l2 = FakeBuilder.build({})
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
                    locator1 = FakeBuilder.build({})
                    assertThat(Locator.current).isEqualTo(locator1)
                    while (locator2 == null) {
                    }
                    assertThat(locator1).isNotEqualTo(locator2)
                    assertThat(Locator.current).isNotEqualTo(locator2)
                })

        val t2 = thread(
                start = false,
                block = {
                    locator2 = FakeBuilder.build({})
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
        locator.shutdown()
    }

    @Test
    fun testName() {
        val locator = FakeBuilder.build({})
        assertThat(locator).isNotNull()
        assertThat("fake locator").isSubstringOf(locator.name)
        assertThat("fake locator").isSubstringOf(locator.toString())
        locator.shutdown()
    }

    @Test
    fun testHolder() {
        val locator = FakeBuilder.build({})
        val holder = SingleHolder()
        assertThat(holder.get() is NullLocator)
        assertThat(holder.put(locator))
        assertThat(holder.get() is FakeBuilder.FakeLocator)
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
            val locator = FakeBuilder.build({})
            assertThat(Locator.bean(Locator::class)).isEqualTo(locator)
            assertThat(org.maxur.mserv.frame.java.Locator.bean(Locator::class.java)).isEqualTo(locator)
            assertThat(Locator.bean(Locator::class, "")).isEqualTo(locator)
            assertThat(org.maxur.mserv.frame.java.Locator.bean(Locator::class.java, "")).isEqualTo(locator)

            assertThat(Locator.beans(Locator::class)).isEqualTo(listOf(locator))
            assertThat(org.maxur.mserv.frame.java.Locator.beans(Locator::class.java)).isEqualTo(listOf(locator))

            assertThat(Locator.bean<Locator>(object : KParameter {
                override val annotations: List<Annotation> = emptyList()
                override val index: Int = 0
                override val isOptional: Boolean = false
                override val isVararg: Boolean = false
                override val kind: KParameter.Kind = KParameter.Kind.VALUE
                override val name: String? = "value"
                override val type: KType = Locator::class.createType()
            })).isEqualTo(locator)
            Locator.stop()
            // Idempotent
            Locator.stop()
        }
    }

    @Test
    fun testLocate() {
        val locator = FakeBuilder.build({})
        assertThat(locator.service(Locator::class.java, "")).isEqualTo(locator)
        assertFailsWith<IllegalStateException> {
            assertThat(locator.locate(FakeBuilder.FakeLocator::class.java, ""))
        }
    }

    @Test
    fun testNames() {
        val locator = FakeBuilder.build({})
        assertThat(locator.names(Locator::class.java)).isEqualTo(listOf(""))
        assertThat(locator.names(LocatorImpl::class.java)).isEqualTo(emptyList<String>())
    }

    @Test
    fun testProperty() {
        val locator = FakeBuilder.build({})
        assertThat(locator.property("key", String::class.java)).isEqualTo("value")
        assertThat(locator.property("invalidkey", String::class.java)).isNull()
        assertThat(locator.property("key", FakeBuilder.FakeLocator::class.java)).isNull()
    }

    object FakeBuilder : LocatorBuilder() {

        override fun make(): Locator = Locator(FakeLocator(name))

        override fun configure(locator: LocatorImpl, function: LocatorConfig.() -> Unit) =
                object : LocatorConfig(locator) {
                    override fun <T : Any> makeDescriptor(bean: Bean<T>, contract: Contract<T>?): Descriptor<T> =
                            object : Descriptor<T>(bean, mutableSetOf(contract!!)) {
                                override fun toSpecificContract(contract: Any) {}
                            }
                    override fun apply() = Unit
                }

        @Suppress("UNCHECKED_CAST")
        class FakeLocator(val str: String, override val name: String = "fake $str") : LocatorImpl {

            override fun inject(injectMe: Any) {
            }

            override fun config(): LocatorConfig = object : LocatorConfig(this) {
                override fun <T : Any> makeDescriptor(bean: Bean<T>, contract: Contract<T>?): Descriptor<T> =
                        object : Descriptor<T>(bean, mutableSetOf(contract!!)) {
                            override fun toSpecificContract(contract: Any) {}
                        }
                override fun apply() = Unit
            }

            override fun configurationError() = null

            override fun <T> service(contractOrImpl: Class<T>, name: String?): T? =
                    if (contractOrImpl == Locator::class.java) Locator(this) as T else null

            override fun <T> services(contractOrImpl: Class<T>): List<T> =
                    if (contractOrImpl == Locator::class.java) listOf(Locator(this)) as List<T> else emptyList()

            override fun names(contractOrImpl: Class<*>): List<String> =
                    if (contractOrImpl == Locator::class.java) listOf("") else emptyList()

            override fun <T> property(key: String, clazz: Class<T>): T? =
                    if (clazz == String::class.java && key == "key") "value" as T else null

            override fun <T> implementation(): T = Object() as T

            override fun close() = Unit
        }
    }
}
