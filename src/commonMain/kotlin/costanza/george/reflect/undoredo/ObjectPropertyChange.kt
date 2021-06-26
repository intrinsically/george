package costanza.george.reflect.undoredo

import costanza.george.diagrams.base.Diagram
import costanza.george.reflect.IObject
import costanza.george.reflect.ObjectTypeRegistry
import costanza.george.reflect.ReflectInfo
import costanza.george.reflect.TokenProvider
import costanza.george.reflect.typedproperties.StringProperty

/** change the property of an entity */
class ObjectPropertyChange(
    entity: IObject,
    var propName: String,
    var oldValue: String,
    var newValue: String
) : IChange, ReflectInfo("objectpropertychange") {
    var entityId = entity.reflectInfo().id!!
    val prop_entityId = StringProperty(this, "entityId", false, "", {entityId}, {entityId = it})
    var prop_propName = StringProperty(this, "propName", false, "", {propName}, {propName = it})
    var prop_oldValue = StringProperty(this, "oldValue", false, "", {oldValue}, {oldValue = it})
    var prop_newValue = StringProperty(this, "newValue", false, "", {newValue}, {newValue = it})


    override fun reflectInfo() = this

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