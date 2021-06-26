package costanza.george.reflect.undoredo

import costanza.george.diagrams.base.Diagram
import costanza.george.reflect.IObject
import costanza.george.reflect.ObjectTypeRegistry
import costanza.george.reflect.TokenProvider

/** change the property of an entity */
class ObjectPropertyChange(
    entity: IObject,
    val propName: String,
    val oldValue: String,
    val newValue: String
) : IChange {
    val id = entity.reflectInfo().id!!

    override fun undo(registry: ObjectTypeRegistry, diagram: Diagram) {
        val prop = ChangeUtilities.findProperty(diagram, id, propName)
        prop.set(TokenProvider(oldValue))
    }

    override fun redo(registry: ObjectTypeRegistry, diagram: Diagram) {
        val prop = ChangeUtilities.findProperty(diagram, id, propName)
        prop.set(TokenProvider(newValue))
    }

    override fun toString() =
        "ObjectProjectyChange(obj = $id, property = $propName, old val = $oldValue, new val = $newValue)"
}