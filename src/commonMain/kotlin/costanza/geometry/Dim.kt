package costanza.geometry

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class Dim(val width: Double, val height: Double) {
    constructor(width: Int, height: Int): this(width.toDouble(), height.toDouble())
}