package costanza.diagrams.base

import com.github.nwillc.ksvg.elements.SVG
import costanza.geometry.Coord
import diagrams.base.Box
import diagrams.base.Diagram
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
abstract class BasicBox: Box() {
    @Transient
    var parentOffset = Coord(0,0)
    var zIndex = 0

    override fun determineZIndex() = zIndex

    override fun prepare(diagram: Diagram, svg: SVG, addedElements: MutableSet<String>, parentOffset: Coord) {
        this.parentOffset = parentOffset
    }
}