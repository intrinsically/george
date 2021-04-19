package costanza.reflect.typedproperties

import costanza.geometry.Coord
import costanza.reflect.IPrimitiveProperty
import costanza.reflect.IProvider

class CoordProperty(
    override val name: String,
    private val isConstructor: Boolean,
    private val defaultValue: Coord,
    val getter: () -> Coord,
    val setter: (c: Coord) -> Unit): IPrimitiveProperty {

    override fun isConstructor() = isConstructor
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