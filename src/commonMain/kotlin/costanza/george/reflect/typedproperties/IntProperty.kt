package costanza.george.reflect.typedproperties

import costanza.george.reflect.IProvider
import costanza.george.reflect.PrimitiveProperty
import costanza.george.reflect.ReflectBase

class IntProperty(
    ri: ReflectBase,
    name: String,
    isConstructor: Boolean,
    var defaultValue: Int,
    var getter: () -> Int,
    var setter: (s: Int) -> Unit
) : PrimitiveProperty(name, isConstructor) {

    init {
        ri.properties += this
    }

    override fun isDefault() = defaultValue == getter()
    override fun get() = getter().toString()
    override fun set(prov: IProvider) = setter(prov.popInt())
}
