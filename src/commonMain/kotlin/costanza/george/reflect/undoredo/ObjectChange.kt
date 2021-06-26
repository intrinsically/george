package costanza.george.reflect.undoredo

import costanza.george.diagrams.base.Diagram
import costanza.george.reflect.IObject
import costanza.george.reflect.ObjectTypeRegistry
import costanza.george.reflect.TokenProvider
import costanza.george.reflect.operations.Deserializer
import costanza.george.reflect.operations.Serializer

/** change an entity living inside a parent entity. TBD */
class ObjectChange(
    obj: IObject,
    val propName: String,
    value: IObject?
) : IChange {

    override fun undo(registry: ObjectTypeRegistry, diagram: Diagram) {
    }

    override fun redo(registry: ObjectTypeRegistry, diagram: Diagram) {
    }
}