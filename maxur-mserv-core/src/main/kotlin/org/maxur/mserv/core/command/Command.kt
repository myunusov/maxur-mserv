package org.maxur.mserv.core.command

import io.swagger.annotations.ApiModelProperty
import org.hibernate.validator.constraints.NotBlank
import javax.validation.constraints.Pattern

/** The Command with some Application Logic */
abstract class Command(
    /** The command type */
    @ApiModelProperty(
        dataType = "string",
        name = "type",
        value = "type of the command",
        notes = "Type of the command",
        required = true,
        allowableValues = "stop, restart",
        example = "restart"
    )
    @NotBlank
    @Pattern(regexp = "^(stop|restart)$")
    val type: String
) {
    private val events = ArrayList<Event>()

    /** Execute command */
    fun execute(): List<Event> {
        run()
        return events
    }

    protected fun post(event: Event) {
        this.events.add(event)
    }

    protected fun post(events: List<Event>) {
        this.events.addAll(events)
    }

    protected abstract fun run()
}