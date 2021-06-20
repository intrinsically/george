package costanza.george.reflect.undoredo

import costanza.george.reflect.IObject
import costanza.george.reflect.operations.findEntityListProperty

class ObjectDelete(
    val entity: IObject,
    val propName: String,
    val index: Int,
) : IChange {
    fun prop() = findEntityListProperty(entity, propName) ?: throw Exception("Cannot find entity list property $propName")
    val child = prop().list[index]

    override fun undo() {
        prop().list.add(index, entity)
    }

    override fun redo() {
        prop().list.removeAt(index)
    }
}