package costanza.george.reflect.typedproperties

import costanza.george.reflect.IProvider
import costanza.george.reflect.PrimitiveProperty
import costanza.george.reflect.ReflectInfo

class OptionalStringProperty(
    ri: ReflectInfo,
    name: String,
    isConstructor: Boolean,
    var defaultValue: String?,
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
