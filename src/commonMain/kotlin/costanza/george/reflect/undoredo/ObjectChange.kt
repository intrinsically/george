package costanza.george.reflect.undoredo

import costanza.george.diagrams.base.Diagram
import costanza.george.ecs.Entity
import costanza.george.reflect.IReflect
import costanza.george.reflect.ObjectTypeRegistry

/** change an entity living inside a parent entity. TBD */
class ObjectChange(
    obj: IReflect?,
    val propName: String,
    value: IReflect?
) : IChange, Entity() {
    override val objectType = "objectchange"

    constructor(): this(null, "", null)

    override fun undo(registry: ObjectTypeRegistry, diagram: Diagram) {
    }

    override fun redo(registry: ObjectTypeRegistry, diagram: Diagram) {
    }
}