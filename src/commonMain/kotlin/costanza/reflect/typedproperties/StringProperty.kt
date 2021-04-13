package costanza.reflect.typedproperties

import costanza.reflect.IProperty

class StringProperty(
    override val name: String,
    val getter: () -> String,
    val setter: (s: String) -> Unit): IProperty {

    override fun get() = getter()
    override fun set(s: String) = setter(s)
}