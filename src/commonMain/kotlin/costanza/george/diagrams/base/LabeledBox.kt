package costanza.george.diagrams.base

import ksvg.elements.SVG
import costanza.george.geometry.Coord
import costanza.george.geometry.Rect

open class LabeledBox(var x: Double = 0.0, var y: Double = 0.0, var width: Double = 0.0, var height: Double = 0.0): BasicBox() {
    override val objectType = "labelledbox"

    private var textHeight = 0.0
    private var textWidth = 0.0

    override fun prepare(diagram: Diagram, svg: SVG, addedElements: MutableSet<String>, parentOffset: Coord) {
        super.prepare(diagram, svg, addedElements, parentOffset)
        textHeight = diagram.calcHeight(FontDetails.NAME)
        textWidth = diagram.calcWidth(FontDetails.NAME, width, name ?: "")
    }

    override fun bounds(diagram: Diagram): Rect {
        return Rect(x, y, width, height)
    }

    override fun add(diagram: Diagram, svg: SVG, zIndex: Int) {
        // draw a red square if in debug mode, and place text
        val b = bounds(diagram)
        if (diagram.debug) {

            svg.rect {
                stroke = "red"
                fill = "none"
                x = "${b.x}"; y = "${b.y}"; width = "${b.width}"; height = "${b.height}"
            }
        }
    }
}