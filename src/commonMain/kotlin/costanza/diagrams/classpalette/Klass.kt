package costanza.diagrams.classpalette

import ksvg.elements.SVG
import costanza.diagrams.base.BasicBox
import costanza.diagrams.base.Container
import costanza.diagrams.base.FontDetails
import costanza.diagrams.base.Part
import costanza.geometry.Coord
import costanza.geometry.Rect
import diagrams.base.Diagram
import diagrams.base.Shape
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import costanza.utility.*
import kotlin.math.max

const val PADDING = 7.0

@Serializable
@SerialName("class")
class Klass(var x: Double = 0.0, var y: Double = 0.0, var parts:_List<Part> = _list()): BasicBox() {
    var stereotype: String? = null
    var width: Double = 150.0 // minimum
    var height: Double = 0.0 // minimum

    /** font details for the different parts */
    @Transient
    private val fontN = FontDetails.NAME
    @Transient
    private val fontS = FontDetails.SUB
    /** set during prepare */
    @Transient
    private var heightN: Double = 0.0
    @Transient
    private var heightS: Double = 0.0
    @Transient
    private var widthM: Double = 0.0

    override fun type(): String {
        return "Class"
    }

    /** get the width and height details */
    override fun prepare(diagram: Diagram, svg: SVG, addedElements: _Set<String>, parentOffset: Coord) {
        super.prepare(diagram, svg, addedElements, parentOffset)
        heightN = diagram.calcHeight(fontN)
        heightS = diagram.calcHeight(fontS) + 3
        widthM = diagram.calcWidth(fontN, width, name ?: "")
        widthM = diagram.calcWidth(fontS, widthM, stereotype ?: "")
        parts.forEach {
            widthM = diagram.calcWidth(fontS, widthM, it.details ?: "")
        }
        widthM += PADDING * 2
    }

    /** use the name as the id to find the shape */
    override fun findShape(nameOrId: String): Shape? {
        if (this.id !== null) {
            return if (this.id == nameOrId) this else null
        }
        return if (nameOrId === name) this else null
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
        return Rect(x, y, widthM, max(actual, height)) + parentOffset
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
                body = it.details ?: ""
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
                body = it.details ?: ""
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

    override fun collectParts(): MutableList<Part>? = parts
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
