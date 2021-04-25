package costanza.reflect.operations.changes

import costanza.reflect.IReflect
import costanza.reflect.operations.findEntityListProperty

class EntityListMove(
    val entity: IReflect,
    val propName: String,
    val from: Int,
    val to: Int
    ) : IChange {
    fun prop() = findEntityListProperty(entity, propName) ?: throw Exception("Cannot find entity list property $propName")

        /** going back is same as going fwd ;-P */
        override fun back() = fwd()

        override fun fwd() {
            val ls = prop().list
            val obj = ls[from]
            ls[from] = ls[to]
            ls[to] = obj
        }
    }