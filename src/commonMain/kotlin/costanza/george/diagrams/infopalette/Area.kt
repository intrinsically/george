package costanza.george.diagrams.infopalette

import costanza.george.diagrams.base.Container
import costanza.george.diagrams.base.Diagram
import costanza.george.diagrams.base.FontDetails
import costanza.george.geometry.Coord
import costanza.george.reflect.typedproperties.IntProperty
import ksvg.elements.SVG

class Area(): Container() {
    override val objectType = "area"

    var zIndex = 0
    var prop_zIndex = IntProperty(this, "zIndex", false, 0, { zIndex }, { zIndex = it })

    override fun determineZIndex() = zIndex

    operator fun String.unaryPlus() {
        name = this
    }

    override fun add(diagram: Diagram, svg: SVG, zIndex: Int) {
        // wait until its our turn
        if (this.zIndex == zIndex) {
            val bounds = bounds(diagram)
            svg.rect {
                x = "${bounds.x}"
                y = "${bounds.y1}"
                width = "${bounds.width}"
                height = "${bounds.height}"
                fill = "white"
                stroke = "black"
                this.fill = "#e0e0e0e0"
            }
            svg.text {
                body = name ?: ""
                x = "${bounds.x + bounds.width / 2}"
                y = "${bounds.y + textHeight}"
                fontFamily = "helvetica"
                fontSize = "18px"
                fontWeight = "bold"
                this.attributes["text-anchor"] = "middle"
                this.attributes["dominant-baseline"] = "central"
            }
        }

        // still do the children, even if we aren't drawn
        super.add(diagram, svg, zIndex)
    }

    private var textHeight: Double = 0.0
    override fun prepare(diagram: Diagram, svg: SVG, addedElements: MutableSet<String>, parentOffset: Coord) {
        super.prepare(diagram, svg, addedElements, parentOffset)
        textHeight = diagram.calcHeight(FontDetails.NAME)
    }
}

fun Container.area(name: String = "", block: (Area.() -> Unit)? = null): Area {
    val area = Area()
    area.name = name
    shapes.add(area)
    if (block !== null) {
        area.apply(block)
    }
    return area
}
