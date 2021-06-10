package costanza.reflect.typedproperties

import costanza.geometry.Coord
import costanza.geometry.Dim
import costanza.geometry.Rect
import costanza.reflect.*

class DimProperty(
    name: String, isConstructor: Boolean, val defaultValue: Dim,
    val getter: () -> Dim,
    val setter: (s: Dim) -> Unit
) : PrimitiveProperty(name, isConstructor) {

    override fun isDefault() = defaultValue == getter()

    override fun get(): String {
        val c = getter()
        return "(${c.width}, ${c.height})"
    }

    override fun set(prov: IProvider) {
        prov.popChar('(')
        val width = prov.popDouble()
        prov.popChar(',')
        val height = prov.popDouble()
        prov.popChar(')')
        setter(Dim(width, height))
    }
}

fun ReflectInfo.dim(name: String, isConstructor: Boolean, defaultValue: Dim, getter: () -> Dim, setter: (s:Dim) -> Unit)
        = properties.add(DimProperty(name, isConstructor, defaultValue, getter, setter))
