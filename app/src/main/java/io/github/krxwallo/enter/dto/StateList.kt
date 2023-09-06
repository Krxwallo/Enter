package io.github.krxwallo.enter.dto

import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "state", strict = false)
data class StateList(
    @field:ElementList(inline = true, entry = "device")
    var devices: List<Device>? = null
)