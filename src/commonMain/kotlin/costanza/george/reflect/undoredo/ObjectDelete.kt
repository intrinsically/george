package costanza.george.reflect.undoredo

import costanza.george.diagrams.base.Diagram
import costanza.george.reflect.IObject
import costanza.george.reflect.ObjectTypeRegistry
import costanza.george.reflect.TokenProvider
import costanza.george.reflect.operations.Deserializer
import costanza.george.reflect.operations.Serializer

/** delete an entity */
class ObjectDelete(
    val parent: IObject,
    val listName: String?,
    val obj: IObject,
    val index: Int,
) : IChange {
    val serial = Serializer().serialize(obj)
    val parentId = parent.reflectInfo().id!!
    val id = obj.reflectInfo().id!!

    override fun redo(registry: ObjectTypeRegistry, diagram: Diagram) {
        ChangeUtilities.removeObjectFromDiagram(diagram, id)
    }

    override fun undo(registry: ObjectTypeRegistry, diagram: Diagram) {
        val obj = Deserializer(registry).deserialize<IObject>(TokenProvider(serial))
        ChangeUtilities.addObjectToDiagram(diagram, parentId, listName, obj, index)
    }

    override fun toString() =
        "ObjectDelete(parent = $parentId, index = $index, obj = $serial)"
}
