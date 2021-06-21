package costanza.george.reflect.typedproperties

import costanza.george.geometry.Rect
import costanza.george.reflect.IProvider
import costanza.george.reflect.PrimitiveProperty
import costanza.george.reflect.ReflectInfo

class RectProperty(
    ri: ReflectInfo,
    name: String,
    isConstructor: Boolean,
    val defaultValue: Rect,
    var getter: () -> Rect,
    var setter: (s: Rect) -> Unit
) : PrimitiveProperty(name, isConstructor) {

    init {
        ri.properties += this
    }

    override fun isDefault() = defaultValue == getter()

    override fun get(): String {
        val r = getter()
        return "(${r.x}, ${r.y}, ${r.width}, ${r.height})"
    }

    override fun set(prov: IProvider) {
        prov.popChar('(')
        val x = prov.popDouble()
        prov.popChar(',')
        val y = prov.popDouble()
        prov.popChar(',')
        val width = prov.popDouble()
        prov.popChar(',')
        val height = prov.popDouble()
        prov.popChar(')')
        setter(Rect(x, y, width, height))
    }
}
