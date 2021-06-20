package costanza.george.reflect.typedproperties

import costanza.george.reflect.IProvider
import costanza.george.reflect.PrimitiveProperty
import costanza.george.reflect.ReflectInfo

class OptionalStringProperty(
    ri: ReflectInfo,
    name: String,
    isConstructor: Boolean,
    val defaultValue: String?,
    var getter: () -> String?,
    var setter: (s: String?) -> Unit
) : PrimitiveProperty(name, isConstructor) {

    init {
        ri.properties += this
    }

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
        = OptionalStringProperty(this, name, isConstructor, null, getter, setter)
