package costanza.george.reflect.typedproperties

import costanza.george.geometry.Rect
import costanza.george.reflect.IProvider
import costanza.george.reflect.PrimitiveProperty
import costanza.george.reflect.ReflectInfo

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
