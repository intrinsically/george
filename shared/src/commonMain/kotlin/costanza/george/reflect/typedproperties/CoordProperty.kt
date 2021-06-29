package costanza.george.reflect.typedproperties

import costanza.george.geometry.Coord
import costanza.george.reflect.*

class CoordProperty(
    ri: ReflectInfo,
    name: String,
    isConstructor: Boolean,
    var defaultValue: Coord,
    var getter: () -> Coord,
    var setter: (s: Coord) -> Unit
) : PrimitiveProperty(name, isConstructor) {

    init {
        ri.properties += this
    }

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
