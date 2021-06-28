package costanza.george.diagrams.base

import costanza.george.geometry.Coord
import costanza.george.reflect.ObjectListProperty
import costanza.george.reflect.typedproperties.IntProperty
import costanza.george.reflect.typedproperties.StringProperty
import costanza.george.utility._List
import costanza.george.utility._list

/** draw lines last */
const val LINE_Z_ORDER = 10

abstract class Line(): Shape() {
    override val objectType = "line"

    var from: String = ""
    var prop_from = StringProperty(this, "from", false, "", {from}, {from=it})
    var to: String = ""
    var prop_to = StringProperty(this, "to", false, "", {to}, {to=it})
    var zIndex: Int = LINE_Z_ORDER
    var prop_zIndex = IntProperty(this, "zIndex", false, LINE_Z_ORDER, {zIndex}, {zIndex=it})
    val points: _List<Coord> = _list()
    val prop_points = ObjectListProperty(this, null, points)


    constructor(from: String, to: String): this() {
        this.from = from
        this.to = to
    }

    override fun determineZIndex() = zIndex
}