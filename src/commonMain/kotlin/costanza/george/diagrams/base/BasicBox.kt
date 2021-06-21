package costanza.george.diagrams.base

import ksvg.elements.SVG
import costanza.george.geometry.Coord
import costanza.george.geometry.Dim
import costanza.george.reflect.ReflectInfo
import costanza.george.reflect.reflect
import costanza.george.reflect.typedproperties.IntProperty
import costanza.george.reflect.typedproperties.int

abstract class BasicBox(loc: Coord = Coord(0,0), dim: Dim = Dim(0,0)): Box(loc, dim) {
    override fun entityType() = "basicbox"

    var parentOffset = Coord(0,0)
    var zIndex = 0
    val prop_zIndex = IntProperty(this, "zIndex", false, 0, {zIndex}, {zIndex=it})

    override fun determineZIndex() = zIndex

    override fun prepare(diagram: Diagram, svg: SVG, addedElements: MutableSet<String>, parentOffset: Coord) {
        this.parentOffset = parentOffset
    }
}