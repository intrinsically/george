package costanza.reflect.typedproperties

import costanza.geometry.Rect
import costanza.reflect.IProvider
import costanza.reflect.PrimitiveProperty
import costanza.reflect.ReflectInfo

class IntProperty(
    name: String, isConstructor: Boolean,
    val defaultValue: Int,
    val getter: () -> Int,
    val setter: (s: Int) -> Unit
) : PrimitiveProperty(name, isConstructor) {

    override fun isDefault() = defaultValue == getter()
    override fun get() = getter().toString()
    override fun set(prov: IProvider) = setter(prov.popInt())
}

fun ReflectInfo.int(name: String, isConstructor: Boolean, defaultValue: Int, getter: () -> Int, setter: (s: Int) -> Unit)
        = properties.add(IntProperty(name, isConstructor, defaultValue, getter, setter))
