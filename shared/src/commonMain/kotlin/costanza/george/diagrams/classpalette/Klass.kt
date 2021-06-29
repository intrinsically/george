package costanza.george.diagrams.classpalette

import costanza.george.diagrams.base.*
import costanza.george.geometry.Coord
import costanza.george.geometry.Dim
import costanza.george.geometry.Rect
import costanza.george.reflect.ObjectListProperty
import costanza.george.reflect.typedproperties.OptionalStringProperty
import costanza.george.utility._List
import costanza.george.utility._Set
import costanza.george.utility._list
import ksvg.elements.SVG
import kotlin.collections.set
import kotlin.math.max

const val PADDING = 7.0

class Klass(loc: Coord = Coord(0,0), dim: Dim = Dim(150,0), val parts:_List<Part> = _list()): BasicBox(loc, dim) {
    override fun entityType() = "class"

    var stereotype: String? = null
    var prop_stereotype = OptionalStringProperty(this, "stereotype", false, null, { stereotype }, { stereotype = it })
    var prop_parts = ObjectListProperty(this, null, parts)
    /** font details for the different parts */
    private val fontN = FontDetails.NAME
    private val fontS = FontDetails.SUB
    /** set during prepare */
    private var heightN: Double = 0.0
    private var heightS: Double = 0.0
    private var widthM: Double = 0.0

    init {
        bounds.prop_dim.defaultValue = Dim(150, 0)
    }

    /** get the width and height details */
    override fun prepare(diagram: Diagram, svg: SVG, addedElements: _Set<String>, parentOffset: Coord) {
        super.prepare(diagram, svg, addedElements, parentOffset)
        heightN = diagram.calcHeight(fontN)
        heightS = diagram.calcHeight(fontS) + 3
        widthM = diagram.calcWidth(fontN, bounds.dim.width, name ?: "")
        widthM = diagram.calcWidth(fontS, widthM, stereotype ?: "")
        parts.forEach {
            widthM = diagram.calcWidth(fontS, widthM, it.details)
        }
        widthM += PADDING * 2
    }

    override fun bounds(diagram: Diagram): Rect {
        val aSize = parts.filterIsInstance<Attribute>().size
        val oSize = parts.size - aSize
        val actual = PADDING * 2 + heightN +
                (if (stereotype != null) heightS else 0.0) +
                if (aSize != 0) {
                    PADDING + aSize * heightS + PADDING
                } else {
                    0.0
                } +
                if (oSize != 0) {
                    PADDING + oSize * heightS
                } else {
                    0.0
                } +
                if (aSize + oSize != 0) {
                    PADDING
                } else {
                    0.0
                } +
                if (aSize != 0 && oSize == 0) {
                    -PADDING
                } else {
                    0.0
                }
        return Rect(bounds.loc.x, bounds.loc.y, widthM, max(actual, bounds.dim.height)) + parentOffset
    }

    operator fun String.unaryPlus() {
        name = this
    }

    override fun add(diagram: Diagram, svg: SVG, zIndex: Int) {
        if (this.zIndex != zIndex) {
            return
        }
        val titleHeight = PADDING * 2 + heightN
        val stereoHeight = heightS
        val bounds = bounds(diagram)
        svg.rect {
            x = "${bounds.x}"
            y = "${bounds.y}"
            width = "${bounds.width}"
            height = "${bounds.height}"
            fill = "#efefef"
            stroke = "black"
        }
        var newY = bounds.y
        if (stereotype != null) {
            svg.text {
                body = "\u00AB" + stereotype!! + "\u00BB"
                x = "${bounds.x + bounds.width / 2}"
                y = "${bounds.y + stereoHeight / 1.4}"
                fontS.setSvgDetails(this)
                this.attributes["text-anchor"] = "middle"
                this.attributes["dominant-baseline"] = "central"
            }
            newY += stereoHeight
        }
        svg.text {
            body = name ?: ""
            x = "${bounds.x + bounds.width / 2}"
            y = "${newY + titleHeight / 2}"
            fontFamily = "helvetica"
            fontSize = "18px"
            fontWeight = "bold"
            this.attributes["text-anchor"] = "middle"
            this.attributes["dominant-baseline"] = "central"
        }
        newY += titleHeight

        // add attributes
        val attrs = parts.filterIsInstance<Attribute>()
        if (attrs.isNotEmpty()) {
            svg.line {
                x1 = "${bounds.x}"; x2 = "${bounds.x2}"
                y1 = "$newY"; y2 = y1
                stroke = "black"
            }
            newY += PADDING
        }
        attrs.forEach {
            svg.text {
                body = it.details
                x = "${bounds.x + PADDING}"
                y = "${newY + heightS * 0.5}"
                fontFamily = "helvetica"
                fontSize = "16px"
                attributes["text-anchor"] = "start"
                attributes["dominant-baseline"] = "central"
            }
            newY += heightS
        }
        if (attrs.isNotEmpty()) {
            newY += PADDING
        }

        // add operations
        val opers = parts.filterIsInstance<Operation>()
        if (opers.isNotEmpty()) {
            svg.line {
                x1 = "${bounds.x}"; x2 = "${bounds.x2}"
                y1 = "$newY"; y2 = y1
                stroke = "black"
            }
        }
        newY += PADDING
        opers.forEach {
            svg.text {
                body = it.details
                x = "${bounds.x + PADDING}"
                y = "${newY + heightS * 0.5}"
                fontFamily = "helvetica"
                fontSize = "16px"
                this.attributes["text-anchor"] = "start"
                this.attributes["dominant-baseline"] = "central"
            }
            newY += heightS
        }
    }

    override fun collectParts(): MutableList<Part> = parts
}


fun Container.klass(name: String, block: (Klass.() -> Unit)? = null): Klass {
    val cls = Klass()
    cls.name = name
    shapes.add(cls)
    if (block !== null) {
        cls.apply(block)
    }
    return cls
}

fun Klass.attribute(details: String) {
    // find the right place, at the end of the attributes
    val index = parts.filterIsInstance<Attribute>().size
    parts.add(index, Attribute(details))
}

fun Klass.operation(details: String) = parts.add(Operation(details))
