package costanza.george.reflect.undoredo

import costanza.george.diagrams.base.Diagram
import costanza.george.reflect.IObject
import costanza.george.reflect.ObjectTypeRegistry
import costanza.george.reflect.TokenProvider
import costanza.george.reflect.operations.Deserializer
import costanza.george.reflect.operations.Serializer

/** create a new entity inside a parent */
class ObjectCreate(
    parent: IObject,
    val listName: String?,
    obj: IObject,
    val index: Int
) : IChange {
    // save the info
    val serial = Serializer().serialize(obj)
    val parentId = parent.reflectInfo().id!!
    val id = obj.reflectInfo().id!!

    override fun undo(registry: ObjectTypeRegistry, diagram: Diagram) {
        // be resilient even if the index changes
        ChangeUtilities.removeObjectFromDiagram(diagram, id, false)
    }

    override fun redo(registry: ObjectTypeRegistry, diagram: Diagram) {
        val obj = Deserializer(registry).deserialize<IObject>(TokenProvider(serial))
        ChangeUtilities.addObjectToDiagram(diagram, parentId, listName, obj, index)
    }

    override fun toString() =
        "ObjectCreate(parent = $parentId, index = $index, obj = $serial)"
}