package costanza.george.diagrams.base

import costanza.george.ecs.Entity
import costanza.george.reflect.typedproperties.OptionalStringProperty
import costanza.george.reflect.typedproperties.StringProperty


abstract class Part: Entity() {
    override val objectType = "part"

    /** details to display on the screen */
    var details: String = ""
    var details_prop = StringProperty(this, "details", true, "", {details}) { details = it }
}