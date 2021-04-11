package costanza.diagrams.base

import kotlinx.serialization.Serializable

@Serializable
abstract class Part {
    /** details to display on the screen */
    var details: String? = null

    /** get the shape type, allowing this to be overridden */
    open fun type() = this::class.simpleName ?: ""
}