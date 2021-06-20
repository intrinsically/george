package costanza.george.reflect.typedproperties

import costanza.george.geometry.Coord
import costanza.george.reflect.IProvider
import costanza.george.reflect.PrimitiveProperty
import costanza.george.reflect.ReflectInfo

class DoubleProperty(
    ri: ReflectInfo,
    name: String,
    isConstructor: Boolean,
    val defaultValue: Double,
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

fun ReflectInfo.double(name: String, isConstructor: Boolean, defaultValue: Double, getter: () -> Double, setter: (s: Double) -> Unit)
        = DoubleProperty(this, name, isConstructor, defaultValue, getter, setter)
