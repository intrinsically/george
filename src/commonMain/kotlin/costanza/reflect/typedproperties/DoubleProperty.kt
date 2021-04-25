package costanza.reflect.typedproperties

import costanza.geometry.Coord
import costanza.reflect.IProvider
import costanza.reflect.PrimitiveProperty
import costanza.reflect.ReflectInfo

class DoubleProperty(
    name: String, isConstructor: Boolean,
    val defaultValue: Double,
    val getter: () -> Double,
    val setter: (s: Double) -> Unit
) : PrimitiveProperty(name, isConstructor) {

    override fun isDefault() = defaultValue == getter()
    override fun get() = getter().toString()
    override fun set(prov: IProvider) = setter(prov.popDouble())
}

fun ReflectInfo.double(name: String, isConstructor: Boolean, defaultValue: Double, getter: () -> Double, setter: (s: Double) -> Unit)
        = properties.add(DoubleProperty(name, isConstructor, defaultValue, getter, setter))
