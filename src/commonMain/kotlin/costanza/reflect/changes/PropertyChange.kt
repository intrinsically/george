package costanza.reflect.changes

import costanza.reflect.IReflect
import costanza.reflect.TokenProvider
import costanza.reflect.operations.findPrimitiveProperty

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