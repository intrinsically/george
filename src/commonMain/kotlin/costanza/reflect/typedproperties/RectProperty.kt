package costanza.reflect.typedproperties

import costanza.geometry.Rect
import costanza.reflect.IProvider
import costanza.reflect.PrimitiveProperty
import costanza.reflect.ReflectInfo

class RectProperty(
    name: String, isConstructor: Boolean, val defaultValue: Rect,
    val getter: () -> Rect,
    val setter: (s: Rect) -> Unit
) : PrimitiveProperty(name, isConstructor) {

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
fun ReflectInfo.rect(name: String, isConstructor: Boolean, defaultValue: Rect, getter: () -> Rect, setter: (s: Rect) -> Unit)
        = properties.add(RectProperty(name, isConstructor, defaultValue, getter, setter))
