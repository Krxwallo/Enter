package io.github.krxwallo.enter.dto

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "channel", strict = false)
data class Channel(
    @field:Attribute(name = "name", required = false)
    var name: String? = null,

    @field:Attribute(name = "type", required = false)
    var type: String? = null,

    @field:Attribute(name = "address", required = false)
    var address: String? = null,

    @field:Attribute(name = "ise_id", required = false)
    var iseId: String? = null,

    @field:Attribute(name = "direction", required = false)
    var direction: String? = null,

    @field:Attribute(name = "parent_device", required = false)
    var parentDevice: String? = null,

    @field:Attribute(name = "index", required = false)
    var index: String? = null,

    @field:Attribute(name = "group_partner", required = false)
    var groupPartner: String? = null,

    @field:Attribute(name = "aes_available", required = false)
    var aesAvailable: String? = null,

    @field:Attribute(name = "transmission_mode", required = false)
    var transmissionMode: String? = null,

    @field:Attribute(name = "visible", required = false)
    var visible: String? = null,

    @field:Attribute(name = "ready_config", required = false)
    var readyConfig: String? = null,

    @field:Attribute(name = "operate", required = false)
    var operate: String? = null,

    @field:ElementList(inline = true, entry = "datapoint", required = false)
    var states: List<Datapoint>? = null
)