package costanza.george.diagrams.classpalette

import costanza.george.diagrams.base.*
import costanza.george.geometry.Coord
import costanza.george.geometry.Dim
import costanza.george.geometry.Rect
import costanza.george.geometry.Router
import costanza.george.reflect.ObjectListProperty
import costanza.george.reflect.typedproperties.BoolProperty
import costanza.george.reflect.typedproperties.DoubleProperty
import costanza.george.reflect.typedproperties.OptionalStringProperty
import costanza.george.reflect.typedproperties.StringProperty
import costanza.george.utility._List
import costanza.george.utility._list
import ksvg.elements.SVG

enum class CompositionType(val marker: String) {
    NONE("none"),
    AGGREGATION("aggregation"),
    COMPOSITION("composition")
}

class Association(): Line() {
    override fun entityType() = "association"

    var label: String? = null
    val prop_label = OptionalStringProperty(this, "label", false, null, { label }, { label = it })
    var composition: CompositionType = CompositionType.NONE
    val prop_composition = StringProperty(this, "composition", false, CompositionType.NONE.name, { composition.name }, { composition = CompositionType.valueOf(it) })
    var arrow: Boolean = false
    val prop_arrow = BoolProperty(this, "arrow", false, { arrow }, { arrow = it })
    var startLabel: String? = null
    val prop_startLabel = OptionalStringProperty(this, "startLabel", false, null, { startLabel }, { startLabel = it })
    var startMult: String? = null
    val prop_startMult = OptionalStringProperty(this, "startMult", false, null, { startMult }, { startMult = it })
    var startXOffset: Double = 0.0
    val prop_startXOffset = DoubleProperty(this, "startXOffset", false, 0.0, { startXOffset }, { startXOffset = it })
    var startYOffset: Double = 0.0
    val prop_startYOffset = DoubleProperty(this, "startYOffset", false, 0.0, { startYOffset }, { startYOffset = it })
    var endLabel: String? = null
    val prop_endLabel = OptionalStringProperty(this, "endLabel", false, null, { endLabel }, { endLabel = it })
    var endMult: String? = null
    val prop_endMult = OptionalStringProperty(this, "endMult", false, null, { endMult }, { endMult = it })
    var endXOffset: Double = 0.0
    val prop_endXOffset = DoubleProperty(this, "endXOffset", false, 0.0, { endXOffset }, { endXOffset = it })
    var endYOffset: Double = 0.0
    val prop_endYOffset = DoubleProperty(this, "endYOffset", false, 0.0, { endYOffset }, { endYOffset = it })

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
        val fromBox = diagram.findShape(from)
        val toBox = diagram.findShape(to)
        val none = Rect(0,0,0,0)
        val x: Shape
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