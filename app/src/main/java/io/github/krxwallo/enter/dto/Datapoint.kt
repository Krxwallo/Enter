package io.github.krxwallo.enter.dto

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Root

@Root(name = "datapoint", strict = false)
data class Datapoint(
    @field:Attribute(name = "name", required = false)
    var name: String? = null,

    @field:Attribute(name = "type", required = false)
    var type: String? = null,

    @field:Attribute(name = "ise_id", required = false)
    var iseId: String? = null,

    @field:Attribute(name = "value", required = false)
    var value: String? = null,

    @field:Attribute(name = "valuetype", required = false)
    var valueType: String? = null,

    @field:Attribute(name = "valueunit", required = false)
    var valueUnit: String? = null,

    @field:Attribute(name = "timestamp", required = false)
    var timestamp: String? = null,

    @field:Attribute(name = "operations", required = false)
    var operations: String? = null
)