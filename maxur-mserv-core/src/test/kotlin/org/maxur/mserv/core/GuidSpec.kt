package org.maxur.mserv.core

import org.assertj.core.api.Assertions
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

@RunWith(JUnitPlatform::class)
class GuidSpec : Spek({

    describe("Build new Guid") {

        context("without arguments") {
            val repository = LocalEntityRepository()
            val id1 = repository.nextId<Any>()
            val id2 = repository.nextId<Any>()

            it("should create new id") {
                Assertions.assertThat(id1).isNotNull()
            }

            it("should create sequence of ids") {
                Assertions.assertThat(id1).isLessThan(id2)
            }

            it("should create sequence of similar ids") {
                Assertions.assertThat(id1.machineIdentifier).isEqualTo(id2.machineIdentifier)
                Assertions.assertThat(id1.processIdentifier).isEqualTo(id2.processIdentifier)
                Assertions.assertThat(id1.timestamp).isLessThanOrEqualTo(id2.timestamp)
                if (id2.counter != 0)
                    Assertions.assertThat(id1.counter).isLessThan(id2.counter)
            }
        }
    }
})
