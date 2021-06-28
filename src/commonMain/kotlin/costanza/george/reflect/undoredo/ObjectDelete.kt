package costanza.george.reflect.undoredo

import costanza.george.diagrams.base.Diagram
import costanza.george.ecs.Entity
import costanza.george.reflect.IObject
import costanza.george.reflect.ObjectTypeRegistry
import costanza.george.reflect.ReflectInfo
import costanza.george.reflect.TokenProvider
import costanza.george.reflect.operations.Deserializer
import costanza.george.reflect.operations.Serializer
import costanza.george.reflect.typedproperties.IntProperty
import costanza.george.reflect.typedproperties.OptionalStringProperty
import costanza.george.reflect.typedproperties.StringProperty

/** delete an entity */
class ObjectDelete(
    var parentId: String = "",
    var listName: String? = null,
    var entityId: String = "",
    var serial: String = "",
    var index: Int = 0
) : IChange, Entity() {
    override fun entityType() = "objectdelete"

    constructor(parent: IObject, listName: String?, obj: IObject, index: Int):
            this(parent.reflectInfo().id!!, listName, obj.reflectInfo().id!!, Serializer().serialize(obj), index)

    val prop_serial = StringProperty(this, "serial", false, "", {serial}, {serial=it})
    val prop_parentId = StringProperty(this, "parentId", false, "", {parentId}, {parentId = it})
    val prop_entityId = StringProperty(this, "entityId", false, "", {entityId}, {entityId = it})
    val prop_listName = OptionalStringProperty(this, "listName", false, null, {listName}, {listName = it})
    val prop_index = IntProperty(this, "index", false, 0, {index}, {index = it})

    override fun redo(registry: ObjectTypeRegistry, diagram: Diagram) {
        ChangeUtilities.removeObjectFromDiagram(diagram, entityId)
    }

    override fun undo(registry: ObjectTypeRegistry, diagram: Diagram) {
        val obj = Deserializer(registry).deserialize<IObject>(TokenProvider(serial))
        ChangeUtilities.addObjectToDiagram(diagram, parentId, listName, obj, index)
    }

    override fun toString() =
        "ObjectDelete(parent = $parentId, index = $index, obj = $serial)"
}
