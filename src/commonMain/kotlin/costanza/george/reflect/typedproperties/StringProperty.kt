package costanza.george.reflect.typedproperties

import costanza.george.reflect.IProvider
import costanza.george.reflect.PrimitiveProperty
import costanza.george.reflect.ReflectInfo

class StringProperty(
    ri: ReflectInfo,
    name: String,
    isConstructor: Boolean,
    var defaultValue: String,
    var getter: () -> String,
    var setter: (s: String) -> Unit
) : PrimitiveProperty(name, isConstructor) {

    init {
        ri.properties += this
    }

    override fun isDefault() = defaultValue == getter()
    override fun get() = "\"${extraSlash(getter())}\""
    override fun set(prov: IProvider) = setter(removeExtraSlash(prov.popString()))

}

/** add an extra \ in front of each quote */
fun extraSlash(str: String) = str.fold("") { acc, ch ->
    if (ch == '\"') {
        acc + "\\\""
    } else {
        acc + ch
    }
}

/** remove one level of slash */
fun removeExtraSlash(str: String): String {
    var previous: Char? = null
    val bld = StringBuilder()
    str.forEach {
        if (previous == '\\' && it == '\"') {
            bld.append(it)
            previous = null
        } else if (previous != null) {
            bld.append(previous)
            previous = it
        } else {
            previous = it
        }
    }
    if (previous != null) {
        bld.append(previous)
    }
    return bld.toString()
}