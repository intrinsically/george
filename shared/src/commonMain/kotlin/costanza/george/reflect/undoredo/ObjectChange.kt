package costanza.george.reflect.undoredo

import costanza.george.diagrams.base.Diagram
import costanza.george.reflect.IObject
import costanza.george.reflect.ObjectTypeRegistry
import costanza.george.reflect.ReflectInfo

/** change an entity living inside a parent entity. TBD */
class ObjectChange(
    obj: IObject,
    val propName: String,
    value: IObject?
) : IChange, ReflectInfo("objectchange") {

    override fun reflectInfo(): ReflectInfo = this

    override fun undo(registry: ObjectTypeRegistry, diagram: Diagram) {
    }

    override fun redo(registry: ObjectTypeRegistry, diagram: Diagram) {
    }
}