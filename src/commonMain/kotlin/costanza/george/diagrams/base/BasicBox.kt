package costanza.george.diagrams.base

import ksvg.elements.SVG
import costanza.george.geometry.Coord
import costanza.george.reflect.ReflectInfo
import costanza.george.reflect.reflect
import costanza.george.reflect.typedproperties.int

abstract class BasicBox: Box() {
    override fun reflectInfo(): ReflectInfo =
        reflect("basicbox", super.reflectInfo()) {
            int("zIndex", false, 0, { zIndex }, { zIndex = it })
        }
    var parentOffset = Coord(0,0)
    var zIndex = 0

    override fun determineZIndex() = zIndex

    override fun prepare(diagram: Diagram, svg: SVG, addedElements: MutableSet<String>, parentOffset: Coord) {
        this.parentOffset = parentOffset
    }
}