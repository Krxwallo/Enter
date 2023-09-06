package io.github.krxwallo.enter.dto

import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "deviceList")
data class DeviceList(
    @field:ElementList(inline = true, entry = "device")
    var devices: List<Device>? = null
)