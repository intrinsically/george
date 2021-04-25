package costanza.reflect.operations.changes

import costanza.reflect.IReflect
import costanza.reflect.operations.findEntityListProperty

class EntityListDelete(
    val entity: IReflect,
    val propName: String,
    val index: Int,
) : IChange {
    fun prop() = findEntityListProperty(entity, propName) ?: throw Exception("Cannot find entity list property $propName")
    val child = prop().list[index]

    override fun back() {
        prop().list.add(index, entity)
    }

    override fun fwd() {
        prop().list.removeAt(index)
    }
}