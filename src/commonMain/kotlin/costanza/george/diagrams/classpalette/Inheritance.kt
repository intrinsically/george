package costanza.george.diagrams.classpalette

import costanza.george.diagrams.base.Container
import costanza.george.diagrams.base.Diagram
import costanza.george.diagrams.base.Line
import costanza.george.geometry.Coord
import costanza.george.geometry.Rect
import costanza.george.geometry.Router
import costanza.george.reflect.ObjectListProperty
import costanza.george.utility._List
import costanza.george.utility._list
import ksvg.elements.SVG

const val INHERITANCE_MARKER = "inheritance_arrow"

class Inheritance(): Line() {
    override val objectType = "inheritance"

    private var parentOffset = Coord(0,0)

    constructor(from: String, to: String) : this() {
        this.from = from
        this.to = to
    }

    override fun prepare(diagram: Diagram, svg: SVG, addedElements: MutableSet<String>, parentOffset: Coord) {
        this.parentOffset = parentOffset
        if (!addedElements.contains(INHERITANCE_MARKER)) {
            svg.marker {
                id = INHERITANCE_MARKER
                viewBox = "0 0 10 10"
                refX = "10"
                refY = "5"
                orient = "auto-start-reverse"
                markerWidth = "10"
                markerHeight = "10"
                path {
                    d = "M 0 0 L 10 5 L 0 10 z"
                    fill = "white"
                    stroke = "black"
                }
            }
            addedElements.add("inheritance_arrow")
        }
    }

    private fun makeRouter(diagram: Diagram): Router {
        val fromBox = diagram.findShape(from)
        val toBox = diagram.findShape(to)
        val none = Rect(0,0,0,0)
        return Router(fromBox?.bounds(diagram) ?: none, toBox?.bounds(diagram) ?: none, points, parentOffset)
    }

    override fun bounds(diagram: Diagram): Rect = makeRouter(diagram).bounds()

    override fun add(diagram: Diagram, svg: SVG, zIndex: Int) {
        if (this.zIndex != zIndex) {
            return
        }
        val fromBox = diagram.findShape(from)
        val toBox = diagram.findShape(to)
        if (fromBox === null || toBox === null) {
            return
        }
        val coords = Router(fromBox.bounds(diagram), toBox.bounds(diagram), points, parentOffset).route()
        svg.polyline {
            points = coords.joinToString(",", transform = { "${it.x},${it.y}" })
            stroke = "black"
            strokeWidth = "2"
            fill = "none"
            markerEnd = "url(#$INHERITANCE_MARKER)"
        }
    }
}

fun Container.inheritance(from: String, to: String, block: (Inheritance.() -> Unit)? = null): Inheritance {
    val inh = Inheritance(from, to)
    shapes.add(inh)
    if (block !== null) {
        inh.apply(block)
    }
    return inh
}