package costanza.george.reflect.typedproperties

import costanza.george.geometry.Coord
import costanza.george.geometry.Dim
import costanza.george.geometry.Rect
import costanza.george.reflect.*

class DimProperty(
    ri: ReflectInfo,
    name: String,
    isConstructor: Boolean,
    var defaultValue: Dim,
    var getter: () -> Dim,
    var setter: (s: Dim) -> Unit
) : PrimitiveProperty(name, isConstructor) {

    init {
        ri.properties += this
    }

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
