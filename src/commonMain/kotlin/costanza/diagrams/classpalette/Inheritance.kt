package costanza.diagrams.classpalette

import ksvg.elements.SVG
import costanza.diagrams.base.Container
import costanza.geometry.Coord
import costanza.geometry.Rect
import costanza.geometry.Router
import diagrams.base.Diagram
import diagrams.base.Line
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

const val INHERITANCE_MARKER = "inheritance_arrow"

@Serializable
@SerialName("inheritance")
class Inheritance(): Line() {
    var points: List<Coord> = listOf()
    @Transient
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
        val fromBox = diagram.findBox(from)
        val toBox = diagram.findBox(to)
        val none = Rect(0,0,0,0)
        return Router(fromBox?.bounds(diagram) ?: none, toBox?.bounds(diagram) ?: none, points, parentOffset)
    }

    override fun bounds(diagram: Diagram): Rect = makeRouter(diagram).bounds()

    override fun add(diagram: Diagram, svg: SVG, zIndex: Int) {
        if (this.zIndex != zIndex) {
            return
        }
        val fromBox = diagram.findBox(from)
        val toBox = diagram.findBox(to)
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