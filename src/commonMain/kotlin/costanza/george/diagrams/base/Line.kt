package costanza.george.diagrams.base

import costanza.george.reflect.ReflectInfo
import costanza.george.reflect.reflect
import costanza.george.reflect.typedproperties.int
import costanza.george.reflect.typedproperties.string

/** draw lines last */
const val LINE_Z_ORDER = 10

abstract class Line(): Shape() {
    override fun reflectInfo(): ReflectInfo =
        reflect("line", super.reflectInfo()) {
            string("from", true, "", { from }, { from = it })
            string("to", true, "", { to }, { to = it })
            int("zIndex", false, LINE_Z_ORDER, { zIndex }, { zIndex = it })
        }

    var from: String = ""
    var to: String = ""
    var zIndex: Int = LINE_Z_ORDER

    constructor(from: String, to: String) : this() {
        this.from = from
        this.to = to
    }

    override fun determineZIndex() = zIndex
}