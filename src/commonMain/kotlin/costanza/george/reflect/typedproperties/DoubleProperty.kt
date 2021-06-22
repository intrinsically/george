package costanza.george.reflect.typedproperties

import costanza.george.geometry.Coord
import costanza.george.reflect.IProvider
import costanza.george.reflect.PrimitiveProperty
import costanza.george.reflect.ReflectInfo

class DoubleProperty(
    ri: ReflectInfo,
    name: String,
    isConstructor: Boolean,
    var defaultValue: Double,
    var getter: () -> Double,
    var setter: (s: Double) -> Unit
) : PrimitiveProperty(name, isConstructor) {

    init {
        ri.properties += this
    }

    override fun isDefault() = defaultValue == getter()
    override fun get() = getter().toString()
    override fun set(prov: IProvider) = setter(prov.popDouble())
}

