package costanza.george.reflect.undoredo

import costanza.george.reflect.IObject
import costanza.george.reflect.operations.findEntityListProperty

data class ObjectDelete(
    val entity: IObject,
    val propName: String?,
    val value: IObject,
    val index: Int,
) : IChange {
    fun prop() = findEntityListProperty(entity, propName) ?: throw Exception("Cannot find entity list property $propName")
    val id = value.reflectInfo().id

    override fun undo() {
        prop().list.add(index, value)
    }

    override fun redo() {
        // be resilient if the index changes
        prop().list.removeAll { it.reflectInfo().id == id }
    }
}