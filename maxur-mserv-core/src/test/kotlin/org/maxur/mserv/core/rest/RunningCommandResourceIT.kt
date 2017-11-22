package org.maxur.mserv.core.rest

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.capture
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.assertj.core.api.Assertions.assertThat
import org.glassfish.hk2.utilities.binding.AbstractBinder
import org.junit.Test
import org.junit.runner.RunWith
import org.maxur.mserv.core.core.command.Command
import org.maxur.mserv.core.core.command.CommandHandler
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.junit.MockitoJUnitRunner
import java.io.IOException
import javax.ws.rs.client.Entity
import javax.ws.rs.core.MediaType
import kotlin.reflect.KClass

@RunWith(MockitoJUnitRunner::class)
class RunningCommandResourceIT : AbstractResourceAT() {

    private var handler = mock<CommandHandler> {
        on { withInjector() } doReturn it
        on { withDelay(any()) } doReturn it
    }

    @Captor
    private lateinit var captor: ArgumentCaptor<Command>

    override fun resourceClass(): KClass<out Any> = RunningCommandResource::class

    override fun configurator(): Function1<AbstractBinder, Unit> = { binder: AbstractBinder ->
        binder.bind(handler).to(CommandHandler::class.java)
    }

    @Test
    @Throws(IOException::class)
    fun testServiceResourceStop() {
        val baseTarget = target("/service/command")
        val response = baseTarget.request()
            .accept(MediaType.APPLICATION_JSON)
            .post(Entity.json("{ \"type\": \"stop\" }"))
        assertThat(response.status).isEqualTo(204)
        verify(handler).handle(capture(captor))
        assertThat(captor.getValue().type).isEqualTo("stop")
    }

    @Test
    @Throws(IOException::class)
    fun testServiceResourceRestart() {
        val baseTarget = target("/service/command")
        val response = baseTarget.request()
            .accept(MediaType.APPLICATION_JSON)
            .post(Entity.json("{ \"type\": \"restart\" }"))
        assertThat(response.status).isEqualTo(204)
        verify(handler).handle(capture(captor))
        assertThat(captor.getValue().type).isEqualTo("restart")
    }
}