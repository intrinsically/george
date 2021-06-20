package costanza.george.reflect.undoredo

import costanza.george.reflect.IObject
import costanza.george.reflect.operations.findEntityListProperty

class ObjectOrderAdjust(
    val entity: IObject,
    val propName: String,
    val from: Int,
    val to: Int
    ) : IChange {
    fun prop() = findEntityListProperty(entity, propName) ?: throw Exception("Cannot find entity list property $propName")

        /** going back is same as going fwd ;-P */
        override fun undo() = redo()

        override fun redo() {
            val ls = prop().list
            val obj = ls[from]
            ls[from] = ls[to]
            ls[to] = obj
        }
    }