package costanza.reflect.typedproperties

import costanza.reflect.IProperty

class IntProperty(
    override val name: String,
    val getter: () -> Int,
    val setter: (s: Int) -> Unit): IProperty {

    override fun get() = getter().toString()
    override fun set(s: String) = setter(s.toInt())
}