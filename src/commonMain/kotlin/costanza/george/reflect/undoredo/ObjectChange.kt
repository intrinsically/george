package costanza.george.reflect.undoredo

import costanza.george.reflect.IObject
import costanza.george.reflect.operations.findObjectProperty

class ObjectChange(val obj: IObject, val propName: String, val value: IObject?) : IChange {
    fun prop() = findObjectProperty(obj, propName) ?: throw Exception("Cannot find property $propName")
    private var old = prop().get()

    override fun undo() {
        prop().set(old)
    }

    override fun redo() {
        prop().set(value)
    }
}