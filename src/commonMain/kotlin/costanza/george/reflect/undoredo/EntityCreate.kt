package costanza.george.reflect.undoredo

import costanza.george.reflect.IReflect
import costanza.george.reflect.operations.findEntityListProperty

class EntityCreate(
    val entity: IReflect,
    val propName: String?,
    val value: IReflect
) : IChange {
    fun prop() = findEntityListProperty(entity, propName) ?: throw Exception("Cannot find entity list property $propName")

    override fun undo() {
        prop().list.removeLast()
    }

    override fun redo() {
        prop().list.add(value)
    }
}