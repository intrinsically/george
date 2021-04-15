package costanza.reflect.typedproperties

import costanza.reflect.IProperty
import costanza.reflect.IProvider

class IntProperty(
    override val name: String,
    private val isConstructor: Boolean,
    private val defaultVal: Int,
    val getter: () -> Int,
    val setter: (s: Int) -> Unit): IProperty {

    override fun isConstructor() = isConstructor
    override fun get() = getter().toString()
    override fun set(prov: IProvider) = setter(prov.popInt())
    override fun isDefault() = getter() == defaultVal
}