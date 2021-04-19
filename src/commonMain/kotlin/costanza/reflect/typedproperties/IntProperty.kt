package costanza.reflect.typedproperties

import costanza.reflect.IPrimitiveProperty
import costanza.reflect.IProvider

class IntProperty(
    override val name: String,
    private val isConstructor: Boolean,
    private val defaultVal: Int,
    val getter: () -> Int,
    val setter: (s: Int) -> Unit): IPrimitiveProperty {

    override fun isConstructor() = isConstructor
    override fun get() = getter().toString()
    override fun set(prov: IProvider) = setter(prov.popInt())
    override fun isDefault() = getter() == defaultVal
}