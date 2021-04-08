package diagrams.base

import kotlinx.serialization.Serializable

/** draw lines last */
const val LINE_Z_ORDER = 10

@Serializable
abstract class Line(): Shape() {
    var from: String = ""
    var to: String = ""
    var zIndex: Int = LINE_Z_ORDER

    constructor(from: String, to: String) : this() {
        this.from = from
        this.to = to
    }

    override fun determineZIndex() = zIndex
}