package costanza.reflect.typedproperties

import costanza.reflect.IProvider
import costanza.reflect.PrimitiveProperty
import costanza.reflect.ReflectInfo

class OptionalStringProperty(
        name: String, isConstructor: Boolean, val defaultValue: String?,
        val getter: () -> String?,
        val setter: (s: String?) -> Unit
    ) : PrimitiveProperty(name, isConstructor) {

    override fun isDefault() = defaultValue == getter()
    override fun get() = if (getter() == null) { "null" } else { "\"" + getter() + "\"" }
    override fun set(prov: IProvider) {
        prov.skip()
        if (prov.peek() == 'n') {
            prov.popName("null")
        } else {
            setter(prov.popString())
        }
    }
}

fun ReflectInfo.optionalString(name: String, isConstructor: Boolean, getter: () -> String?, setter: (s: String?) -> Unit)
        = properties.add(OptionalStringProperty(name, isConstructor, null, getter, setter))
