package costanza.george.reflect.changes

import costanza.george.reflect.IReflect
import costanza.george.reflect.TokenProvider
import costanza.george.reflect.operations.findPrimitiveProperty

class PropertyChange(
    val entity: IReflect,
    propName: String,
    val value: String
) : IChange {
    val prop = findPrimitiveProperty(entity, propName) ?: throw Exception("Cannot find property $propName")
    val old = prop.get()

    override fun back() {
        prop.set(TokenProvider(old))
    }

    override fun fwd() {
        prop.set(TokenProvider(value))
    }
}