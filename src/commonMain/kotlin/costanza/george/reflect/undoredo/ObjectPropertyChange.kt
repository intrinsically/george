package costanza.george.reflect.undoredo

import costanza.george.reflect.IObject
import costanza.george.reflect.TokenProvider
import costanza.george.reflect.operations.findPrimitiveProperty

data class ObjectPropertyChange(
    val entity: IObject,
    val propName: String,
    val oldValue: String,
    val newValue: String
) : IChange {
    val prop = findPrimitiveProperty(entity, propName) ?: throw Exception("Cannot find property $propName")

    override fun undo() {
        prop.set(TokenProvider(oldValue))
    }

    override fun redo() {
        prop.set(TokenProvider(newValue))
    }
}