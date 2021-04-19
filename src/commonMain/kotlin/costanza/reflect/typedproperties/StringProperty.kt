package costanza.reflect.typedproperties

import costanza.reflect.IPrimitiveProperty
import costanza.reflect.IProvider

class StringProperty(
    override val name: String,
    private val isConstructor: Boolean,
    private val defaultValue: String,
    val getter: () -> String,
    val setter: (s: String) -> Unit): IPrimitiveProperty {

    override fun isConstructor() = isConstructor
    override fun isDefault() = defaultValue == getter()
    override fun get() = "\"${getter()}\""
    override fun set(prov: IProvider) = setter(prov.popString())
}