package costanza.george.reflect.undoredo

import costanza.george.diagrams.base.Diagram
import costanza.george.ecs.Entity
import costanza.george.reflect.IObject
import costanza.george.reflect.ObjectTypeRegistry
import costanza.george.reflect.ReflectInfo
import costanza.george.reflect.TokenProvider
import costanza.george.reflect.operations.Deserializer
import costanza.george.reflect.operations.Serializer

/** change an entity living inside a parent entity. TBD */
class ObjectChange(
    obj: IObject?,
    val propName: String,
    value: IObject?
) : IChange, Entity() {
    override fun entityType() = "objectchange"

    constructor(): this(null, "", null)

    override fun undo(registry: ObjectTypeRegistry, diagram: Diagram) {
    }

    override fun redo(registry: ObjectTypeRegistry, diagram: Diagram) {
    }
}