package costanza.george.reflect.typedproperties

import costanza.george.reflect.IProvider
import costanza.george.reflect.PrimitiveProperty
import costanza.george.reflect.ReflectInfo

class BoolProperty(
    name: String,
    isConstructor: Boolean,
    val getter: () -> Boolean,
    val setter: (s: Boolean) -> Unit
) : PrimitiveProperty(name, isConstructor) {

    override fun isDefault() = !getter()
    override fun get() = getter().toString()
    override fun set(prov: IProvider) {
        prov.skip()
        if (prov.peek() == 't') {
            prov.popName("true")
            setter(true)
        } else {
            prov.popName("false")
            setter(false)
        }
    }
}

fun ReflectInfo.bool(name: String, isConstructor: Boolean, getter: () -> Boolean, setter: (s: Boolean) -> Unit)
        = properties.add(BoolProperty(name, isConstructor, getter, setter))
