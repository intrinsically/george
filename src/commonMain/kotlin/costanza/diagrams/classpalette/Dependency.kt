package costanza.diagrams.classpalette

import ksvg.elements.SVG
import costanza.diagrams.base.Container
import costanza.geometry.Coord
import costanza.geometry.Rect
import costanza.geometry.Router
import diagrams.base.Diagram
import costanza.diagrams.base.Line
import costanza.reflect.ReflectInfo
import costanza.reflect.reflect
import costanza.reflect.typedproperties.optionalString

class Dependency(): Line() {
    override fun reflectInfo(): ReflectInfo =
        reflect("dependency", super.reflectInfo()) {
            optionalString("label", false, { label }, { label = it })
        }

    private val END_TYPE = "dependency_arrow"
    private var parentOffset = Coord(0,0)

    var label: String? = null
    var points: List<Coord> = listOf()

    constructor(from: String, to: String) : this() {
        this.from = from
        this.to = to
    }

    /** add the markers */
    override fun prepare(diagram: Diagram, svg: SVG, addedElements: MutableSet<String>, parentOffset: Coord) {
        this.parentOffset = parentOffset
        addMarker(svg, addedElements, END_TYPE, "M 0 0 L 10 5 L 0 10 L 7 5 z", "black", "black")
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
        val coords = makeRouter(diagram).route()
        svg.polyline {
            points = coords.joinToString(",", transform = { "${it.x},${it.y}" })
            stroke = "black"
            strokeWidth = "2"
            fill = "none"
            markerEnd = "url(#$END_TYPE)"
            strokeDasharray = "3 2"
        }
    }
}

fun Container.dependency(from: String, to: String, block: (Dependency.() -> Unit)? = null): Dependency {
    val dep = Dependency(from, to)
    shapes.add(dep)
    if (block != null) {
        dep.apply(block)
    }
    return dep
}