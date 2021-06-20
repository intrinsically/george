package costanza.george.reflect.typedproperties

import costanza.george.reflect.IProvider
import costanza.george.reflect.PrimitiveProperty
import costanza.george.reflect.ReflectInfo

class BoolProperty(
    ri: ReflectInfo,
    name: String,
    isConstructor: Boolean,
    var getter: () -> Boolean,
    var setter: (s: Boolean) -> Unit
) : PrimitiveProperty(name, isConstructor) {

    init {
        ri.properties += this
    }

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
        = BoolProperty(this, name, isConstructor, getter, setter)
