package costanza.george.reflect.typedproperties

import costanza.george.reflect.IProvider
import costanza.george.reflect.PrimitiveProperty
import costanza.george.reflect.ReflectInfo

class StringProperty(
    name: String, isConstructor: Boolean, val defaultValue: String,
    val getter: () -> String,
    val setter: (s: String) -> Unit
) : PrimitiveProperty(name, isConstructor) {

    override fun isDefault() = defaultValue == getter()
    override fun get() = "\"${getter()}\""
    override fun set(prov: IProvider) = setter(prov.popString())
}

fun ReflectInfo.string(name: String, isConstructor: Boolean, defaultValue: String, getter: () -> String, setter: (s: String) -> Unit)
        = properties.add(StringProperty(name, isConstructor, defaultValue, getter, setter))
