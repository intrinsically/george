package costanza.george.reflect.undoredo

import costanza.george.diagrams.base.Diagram
import costanza.george.ecs.Entity
import costanza.george.reflect.IReflect
import costanza.george.reflect.ObjectTypeRegistry
import costanza.george.reflect.TokenProvider
import costanza.george.reflect.typedproperties.StringProperty

/** change the property of an entity */
class ObjectPropertyChange(
    var entityId: String = "",
    var propName: String = "",
    var oldValue: String = "",
    var newValue: String = ""
) : IChange, Entity() {
    override val objectType = "objectpropertychange"

    constructor(entity: IReflect, propName: String, oldValue: String, newValue: String):
            this(entity.id, propName, oldValue, newValue)

    val prop_entityId = StringProperty(this, "entityId", false, "", {entityId}, {entityId = it})
    var prop_propName = StringProperty(this, "propName", false, "", {propName}, {propName = it})
    var prop_oldValue = StringProperty(this, "oldValue", false, "", {oldValue}, {oldValue = it})
    var prop_newValue = StringProperty(this, "newValue", false, "", {newValue}, {newValue = it})

    override fun undo(registry: ObjectTypeRegistry, diagram: Diagram) {
        val prop = ChangeUtilities.findProperty(diagram, entityId, propName)
        prop.set(TokenProvider(oldValue))
    }

    override fun redo(registry: ObjectTypeRegistry, diagram: Diagram) {
        val prop = ChangeUtilities.findProperty(diagram, entityId, propName)
        prop.set(TokenProvider(newValue))
    }

    override fun toString() =
        "ObjectProjectyChange(obj = $id, property = $propName, old val = $oldValue, new val = $newValue)"
}