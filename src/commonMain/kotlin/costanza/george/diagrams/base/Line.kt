package costanza.george.diagrams.base

import costanza.george.reflect.ReflectInfo
import costanza.george.reflect.reflect
import costanza.george.reflect.typedproperties.IntProperty
import costanza.george.reflect.typedproperties.StringProperty
import costanza.george.reflect.typedproperties.int
import costanza.george.reflect.typedproperties.string

/** draw lines last */
const val LINE_Z_ORDER = 10

abstract class Line(): Shape() {
    override fun entityType() = "line"

    var from: String = ""
    var prop_from = StringProperty(this, "from", false, "", {from}, {from=it})
    var to: String = ""
    var prop_to = StringProperty(this, "to", false, "", {to}, {to=it})
    var zIndex: Int = LINE_Z_ORDER
    var prop_zIndex = IntProperty(this, "zIndex", false, LINE_Z_ORDER, {zIndex}, {zIndex=it})

    constructor(from: String, to: String): this() {
        this.from = from
        this.to = to
    }

    override fun determineZIndex() = zIndex
}