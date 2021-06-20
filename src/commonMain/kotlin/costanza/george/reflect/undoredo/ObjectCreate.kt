package costanza.george.reflect.undoredo

import costanza.george.reflect.IObject
import costanza.george.reflect.operations.findEntityListProperty

class ObjectCreate(
    val entity: IObject,
    val propName: String?,
    val value: IObject
) : IChange {
    fun prop() = findEntityListProperty(entity, propName) ?: throw Exception("Cannot find entity list property $propName")

    override fun undo() {
        prop().list.removeLast()
    }

    override fun redo() {
        prop().list.add(value)
    }
}