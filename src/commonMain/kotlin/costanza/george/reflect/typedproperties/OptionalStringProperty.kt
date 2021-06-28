package costanza.george.reflect.typedproperties

import costanza.george.reflect.IProvider
import costanza.george.reflect.PrimitiveProperty
import costanza.george.reflect.ReflectBase

class OptionalStringProperty(
    ri: ReflectBase?,
    name: String,
    isConstructor: Boolean,
    var defaultValue: String?,
    var getter: () -> String?,
    var setter: (s: String?) -> Unit
) : PrimitiveProperty(name, isConstructor) {

    init {
        if (ri != null) {
            ri.properties += this
        }
    }

    override fun isDefault() = defaultValue == getter()
    override fun get() = if (getter() == null) { "null" } else { "\"" + extraSlash(getter()!!) + "\"" }
    override fun set(prov: IProvider) {
        prov.skip()
        if (prov.peek() == 'n') {
            prov.popName("null")
        } else {
            setter(removeExtraSlash(prov.popString()))
        }
    }
}
