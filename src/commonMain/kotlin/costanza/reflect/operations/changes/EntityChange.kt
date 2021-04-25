package costanza.reflect.operations.changes

import costanza.reflect.IReflect
import costanza.reflect.operations.findEntityProperty

class EntityChange(val entity: IReflect, val propName: String, val value: IReflect?) : IChange {
    fun prop() = findEntityProperty(entity, propName) ?: throw Exception("Cannot find property $propName")
    private var old = prop().get()

    override fun back() {
        prop().set(old)
    }

    override fun fwd() {
        prop().set(value)
    }
}