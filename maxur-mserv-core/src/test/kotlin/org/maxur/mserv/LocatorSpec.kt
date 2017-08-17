package org.maxur.mserv

import org.assertj.core.api.Assertions
import org.junit.Test

import org.maxur.mserv.core.Locator
import org.maxur.mserv.core.service.hk2.LocatorFactoryHK2Impl
import kotlin.concurrent.thread


class LocatorSpec {

    @Test
    fun testSingle() {
        val locator = LocatorFactoryHK2Impl {}.make()
        Assertions.assertThat(locator).isNotNull()
        locator.shutdown()
    }

    @Test
    fun testSeq() {
        val l1 = LocatorFactoryHK2Impl {}.make()
        Assertions.assertThat(l1).isNotNull()
        val l2 = LocatorFactoryHK2Impl {}.make()
        Assertions.assertThat(l2).isNotNull()
        Assertions.assertThat(l1).isNotEqualTo(l2)
        Assertions.assertThat(Locator.current).isNotEqualTo(l1)
        Assertions.assertThat(Locator.current).isEqualTo(l2)
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
                    Assertions.assertThat(Locator.current).isEqualTo(locator1)
                    while (locator2 == null) {
                    }
                    Assertions.assertThat(locator1).isNotEqualTo(locator2)
                    Assertions.assertThat(Locator.current).isNotEqualTo(locator2)
                })

        val t2 = thread(
                start = false,
                block = {
                    locator2 = LocatorFactoryHK2Impl {}.make()
                    Assertions.assertThat(Locator.current).isEqualTo(locator2)
                    while (locator1 == null) {
                    }
                    Assertions.assertThat(locator2).isNotEqualTo(locator1)
                    Assertions.assertThat(Locator.current).isNotEqualTo(locator1)
                })

        var error: Throwable? = null

        t1.uncaughtExceptionHandler = Thread.UncaughtExceptionHandler { _, ex -> error = ex }
        t2.uncaughtExceptionHandler = Thread.UncaughtExceptionHandler { _, ex -> error = ex }

        t1.start()
        t2.start()

        t1.join()
        t2.join()

        Assertions.assertThat(Locator.current).isIn(locator1, locator2)

        locator1?.shutdown()
        locator2?.shutdown()

        if (error != null) {
            throw error as Throwable
        }
    }

}
