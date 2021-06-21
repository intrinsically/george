package costanza.george.reflect.typedproperties

import costanza.george.geometry.Rect
import costanza.george.reflect.IProvider
import costanza.george.reflect.PrimitiveProperty
import costanza.george.reflect.ReflectInfo

class IntProperty(
    ri: ReflectInfo,
    name: String,
    isConstructor: Boolean,
    val defaultValue: Int,
    var getter: () -> Int,
    var setter: (s: Int) -> Unit
) : PrimitiveProperty(name, isConstructor) {

    init {
        ri.properties += this
    }

    override fun isDefault() = defaultValue == getter()
    override fun get() = getter().toString()
    override fun set(prov: IProvider) = setter(prov.popInt())
}
