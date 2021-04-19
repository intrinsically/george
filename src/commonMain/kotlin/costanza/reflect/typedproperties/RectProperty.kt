package costanza.reflect.typedproperties

import costanza.geometry.Rect
import costanza.reflect.IPrimitiveProperty
import costanza.reflect.IProvider

class RectProperty(
    override val name: String,
    private val isConstructor: Boolean,
    private val defaultValue: Rect,
    val getter: () -> Rect,
    val setter: (r: Rect) -> Unit): IPrimitiveProperty {

    override fun isConstructor() = isConstructor
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