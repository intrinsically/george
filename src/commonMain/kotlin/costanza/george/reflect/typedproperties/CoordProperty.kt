package costanza.george.reflect.typedproperties

import costanza.george.geometry.Coord
import costanza.george.geometry.Rect
import costanza.george.reflect.*

class CoordProperty(
    name: String, isConstructor: Boolean, val defaultValue: Coord,
    val getter: () -> Coord,
    val setter: (s: Coord) -> Unit
) : PrimitiveProperty(name, isConstructor) {

    override fun isDefault() = defaultValue == getter()

    override fun get(): String {
        val c = getter()
        return "(${c.x}, ${c.y})"
    }

    override fun set(prov: IProvider) {
        prov.popChar('(')
        val x = prov.popDouble()
        prov.popChar(',')
        val y = prov.popDouble()
        prov.popChar(')')
        setter(Coord(x, y))
    }
}

fun ReflectInfo.coord(name: String, isConstructor: Boolean, defaultValue: Coord, getter: () -> Coord, setter: (s:Coord) -> Unit)
     = properties.add(CoordProperty(name, isConstructor, defaultValue, getter, setter))
