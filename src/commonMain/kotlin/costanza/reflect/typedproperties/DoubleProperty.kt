package costanza.reflect.typedproperties

import costanza.reflect.IPrimitiveProperty
import costanza.reflect.IProvider

class DoubleProperty(
    override val name: String,
    private val isConstructor: Boolean,
    private val defaultValue: Double,
    val getter: () -> Double,
    val setter: (s: Double) -> Unit): IPrimitiveProperty {

    override fun isConstructor() = isConstructor
    override fun isDefault() = defaultValue == getter()
    override fun get() = getter().toString()
    override fun set(prov: IProvider) = setter(prov.popDouble())
}