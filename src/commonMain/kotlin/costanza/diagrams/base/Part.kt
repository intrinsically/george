package costanza.diagrams.base


abstract class Part {
    /** details to display on the screen */
    var details: String? = null

    /** get the shape type, allowing this to be overridden */
    open fun type() = this::class.simpleName ?: ""
}