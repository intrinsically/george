package costanza.george.diagrams.base

import costanza.george.ecs.Entity
import costanza.george.reflect.typedproperties.OptionalStringProperty


abstract class Part: Entity() {
    override fun entityType() = "part"

    /** details to display on the screen */
    var details: String? = null
    var details_prop = OptionalStringProperty(this, "details", false, null, {details}) { details = it }

    /** get the shape type, allowing this to be overridden */
    open fun type() = this::class.simpleName ?: ""
}