package costanza.george.reflect.typedproperties

import costanza.george.reflect.IProvider
import costanza.george.reflect.PrimitiveProperty
import costanza.george.reflect.ReflectBase

class StringProperty(
    ri: ReflectBase?,
    name: String,
    isConstructor: Boolean,
    var defaultValue: String,
    var getter: () -> String,
    var setter: (s: String) -> Unit
) : PrimitiveProperty(name, isConstructor) {

    init {
        if (ri != null) {
            ri.properties += this
        }
    }

    override fun isDefault() = defaultValue == getter()
    /** for serialization */
    override fun get() = "\"${extraSlash(getter())}\""
    /** for deserialization */
    override fun set(prov: IProvider) = setter(removeExtraSlash(prov.popString()))

}

/** add an extra \ in front of each quote */
fun extraSlash(str: String): String {
    var prev: Char = ' '
    val bld = StringBuilder()
    str.forEach {
        if (prev != '\\' && (it == '\"' || it == '\\')) {
            bld.append("\\")
        }
        bld.append(it)
        prev = it
    }
    return bld.toString()
}

/** remove one level of slash */
fun removeExtraSlash(str: String): String {
    var previous: Char = ' '
    val bld = StringBuilder()
    str.forEach {
        if (previous == '\\' || it != '\\') {
            bld.append(it)
        }
        previous = it
    }
    return bld.toString()
}