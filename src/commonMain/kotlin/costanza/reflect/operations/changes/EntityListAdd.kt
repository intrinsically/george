package costanza.reflect.operations.changes

import costanza.reflect.IReflect
import costanza.reflect.operations.findEntityListProperty

class EntityListAdd(
    val entity: IReflect,
    val propName: String,
    val value: IReflect
) : IChange {
    fun prop() = findEntityListProperty(entity, propName) ?: throw Exception("Cannot find entity list property $propName")

    override fun back() {
        prop().list.removeLast()
    }

    override fun fwd() {
        prop().list.add(entity)
    }
}