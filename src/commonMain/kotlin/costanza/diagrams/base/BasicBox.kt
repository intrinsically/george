package costanza.diagrams.base

import ksvg.elements.SVG
import costanza.geometry.Coord
import diagrams.base.Box
import diagrams.base.Diagram

abstract class BasicBox: Box() {
    var parentOffset = Coord(0,0)
    var zIndex = 0

    override fun determineZIndex() = zIndex

    override fun prepare(diagram: Diagram, svg: SVG, addedElements: MutableSet<String>, parentOffset: Coord) {
        this.parentOffset = parentOffset
    }
}