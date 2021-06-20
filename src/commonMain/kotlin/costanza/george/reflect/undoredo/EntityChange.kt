package costanza.george.reflect.undoredo

import costanza.george.reflect.IReflect
import costanza.george.reflect.operations.findEntityProperty

class EntityChange(val entity: IReflect, val propName: String, val value: IReflect?) : IChange {
    fun prop() = findEntityProperty(entity, propName) ?: throw Exception("Cannot find property $propName")
    private var old = prop().get()

    override fun undo() {
        prop().set(old)
    }

    override fun redo() {
        prop().set(value)
    }
}