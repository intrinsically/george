package costanza.george.reflect.undoredo

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

    override fun undo() {
        prop.set(TokenProvider(old))
    }

    override fun redo() {
        prop.set(TokenProvider(value))
    }
}