package costanza.reflect.typedproperties

import costanza.reflect.IProvider
import costanza.reflect.PrimitiveProperty
import costanza.reflect.ReflectInfo

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
