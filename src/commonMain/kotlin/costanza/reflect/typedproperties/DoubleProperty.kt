package costanza.reflect.typedproperties

import costanza.reflect.IProperty

class DoubleProperty(
    override val name: String,
    val getter: () -> Double,
    val setter: (s: Double) -> Unit): IProperty {

    override fun get() = getter().toString()
    override fun set(s: String) = setter(s.toDouble())
}