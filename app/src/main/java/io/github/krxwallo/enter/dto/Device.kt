package io.github.krxwallo.enter.dto

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "device", strict = false)
data class Device(
    @field:Attribute(name = "name", required = false)
    var name: String? = null,

    @field:Attribute(name = "address", required = false)
    var address: String? = null,

    @field:Attribute(name = "ise_id", required = false)
    var iseId: String? = null,

    @field:Attribute(name = "interface", required = false)
    var interfaceType: String? = null,

    @field:Attribute(name = "device_type", required = false)
    var deviceType: String? = null,

    @field:Attribute(name = "ready_config", required = false)
    var readyConfig: String? = null,

    @field:ElementList(inline = true, entry = "channel", required = false)
    var channels: List<Channel>? = null
)