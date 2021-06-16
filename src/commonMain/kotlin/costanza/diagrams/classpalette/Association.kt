package costanza.diagrams.classpalette

import ksvg.elements.SVG
import costanza.diagrams.base.Container
import costanza.diagrams.base.FontDetails
import costanza.geometry.Coord
import costanza.geometry.Dim
import costanza.geometry.Rect
import costanza.geometry.Router
import costanza.diagrams.base.Diagram
import costanza.diagrams.base.Line
import costanza.reflect.ReflectInfo
import costanza.reflect.entityList
import costanza.reflect.reflect
import costanza.reflect.typedproperties.bool
import costanza.reflect.typedproperties.double
import costanza.reflect.typedproperties.optionalString
import costanza.reflect.typedproperties.string
import costanza.utility._List
import costanza.utility._list

enum class CompositionType(val marker: String) {
    NONE("none"),
    AGGREGATION("aggregation"),
    COMPOSITION("composition")
}

class Association(): Line() {
    override fun reflectInfo(): ReflectInfo =
        reflect("association", super.reflectInfo()) {
            optionalString("label", false, { label }, { label = it })
            string("composition", false, CompositionType.NONE.name, { composition.name }, { composition = CompositionType.valueOf(it) })
            bool("arrow", false, { arrow }, { arrow = it })
            optionalString("startLabel", false, { startLabel }, { startLabel = it })
            optionalString("startMult", false, { startMult }, { startMult = it })
            double("startXOffset", false, 0.0, { startXOffset }, { startXOffset = it })
            double("startYOffset", false, 0.0, { startYOffset }, { startYOffset = it })
            optionalString("endLabel", false, { endLabel }, { endLabel = it })
            optionalString("endMult", false, { endMult }, { endMult = it })
            double("endXOffset", false, 0.0, { endXOffset }, { endXOffset = it })
            double("endYOffset", false, 0.0, { endYOffset }, { endYOffset = it })
            entityList(points)
        }

    var label: String? = null
    var composition: CompositionType = CompositionType.NONE
    var arrow: Boolean = false
    var startLabel: String? = null
    var startMult: String? = null
    var startXOffset: Double = 0.0
    var startYOffset: Double = 0.0
    var endLabel: String? = null
    var endMult: String? = null
    var endXOffset: Double = 0.0
    var endYOffset: Double = 0.0
    var points: _List<Coord> = _list()

    private var parentOffset = Coord(0,0)

    constructor(from: String, to: String) : this() {
        this.from = from
        this.to = to
    }

    /** add the markers */
    override fun prepare(diagram: Diagram, svg: SVG, addedElements: MutableSet<String>, parentOffset: Coord) {
        this.parentOffset = parentOffset
        addMarker(svg, addedElements, CompositionType.NONE.marker)
        addMarker(svg, addedElements, CompositionType.AGGREGATION.marker, "M 5 1 L 10 5 L 5 9 L 0 5 L 5 1", "black", "black")
        addMarker(svg, addedElements, CompositionType.COMPOSITION.marker, "M 5 1 L 10 5 L 5 9 L 0 5 L 5 1", "black", "white")
        addMarker(svg, addedElements, "arrow", "M 0 0 L 10 5 L 0 10 L 7 5 z", "black", "black")
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
        val router = makeRouter(diagram)
        val coords = router.route()
        svg.polyline {
            points = coords.joinToString(",", transform = { "${it.x},${it.y}" })
            stroke = "black"
            strokeWidth = "2"
            fill = "none"
            markerStart = "url(#${(composition.marker)})"
            markerEnd = "url(#${if (arrow) "arrow" else "none"})"
        }

        drawText(diagram, svg, router, startMult, true, 0)
        drawText(diagram, svg, router, startLabel, true, if (startMult === null) 0 else 1)
        drawText(diagram, svg, router, endMult, false, 0)
        drawText(diagram, svg, router, endLabel, false, if (endMult === null) 0 else 1)
    }

    /** draw the text at the correct point as decided by the router */
    private fun drawText(diagram: Diagram, svg: SVG, router: Router, str: String?, start: Boolean, linesAway: Int) {
        if (str !== null) {
            val textWidth = diagram.calcWidth(FontDetails.LABEL, 0.0, str)
            val textHeight = diagram.calcHeight(FontDetails.LABEL)
            val pt = if (start) {
                router.startText(textHeight, textWidth, true, linesAway)
            } else {
                router.endText(textHeight, textWidth, true, linesAway)
            } + if (start) {
                Dim(startXOffset, startYOffset)
            } else {
                Dim(endXOffset, endYOffset)
            }
            if (diagram.debug) {
                svg.rect {
                    stroke = "red"
                    fill = "none"
                    x = "${pt.x}"; y = "${pt.y}"
                    width = "$textWidth"; height = "$textHeight"
                }
            }
            svg.text {
                body = str
                x = "${pt.x}"
                y = "${pt.y}"
                fontFamily = FontDetails.LABEL.face
                fontSize = "${FontDetails.LABEL.size}px"
                attributes["text-anchor"] = "start"
                attributes["dominant-baseline"] = "hanging"
            }
        }

    }
}

/** add a marker to the SVG document */
fun addMarker(
        svg: SVG,
        addedElements: MutableSet<String>,
        endType: String,
        pathCoords: String? = null,
        stroked: String? = null,
        filled: String? = null
) {
    if (!addedElements.contains(endType)) {
        svg.marker {
            id = endType
            viewBox = "0 0 10 10"
            refX = "10"
            refY = "5"
            orient = "auto-start-reverse"
            markerWidth = "10"
            markerHeight = "10"
            if (pathCoords !== null) {
                path {
                    d = "${pathCoords}"
                    fill = filled ?: "none"
                    stroke = stroked ?: "none"
                }
            }
        }
        addedElements.add(endType)
    }
}

fun Container.association(from: String, to: String, block: (Association.() -> Unit)? = null): Association {
    val assoc = Association(from, to)
    shapes.add(assoc)
    if (block !== null) {
        assoc.apply(block)
    }
    return assoc
}