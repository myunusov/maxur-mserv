#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME}

#end
import org.assertj.core.api.Assertions
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

#parse("Class Header.java")
@RunWith(JUnitPlatform::class)
class ${NAME} : Spek({

    describe("Do something ") {

        context("with ") {
            val sut = ""

            it("should be ") {
                Assertions.assertThat(sut).isNotNull()
            }
        }
    }
})