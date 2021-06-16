package costanza.diagrams.infopalette

import ksvg.elements.SVG
import costanza.diagrams.base.Container
import costanza.diagrams.base.FontDetails
import costanza.geometry.Coord
import costanza.diagrams.base.Diagram
import costanza.diagrams.base.LINE_Z_ORDER
import costanza.geometry.Dim
import costanza.reflect.ReflectInfo
import costanza.reflect.reflect
import costanza.reflect.typedproperties.int
import costanza.reflect.typedproperties.string

class Note(var text: String = ""): Container() {

    override fun reflectInfo(): ReflectInfo =
        reflect("note", super.reflectInfo()) {
            string("text", false, "", { text }, { text = it })
            int("zIndex", false, LINE_Z_ORDER + 10, { zIndex }, { zIndex = it })
        }

    val fillStyle: String = "#fffdd0"
    val strokeStyle: String = "gray"
    var zIndex = LINE_Z_ORDER + 10 // on top of even lines
    override fun determineZIndex() = zIndex

    init {
        dim = Dim(150, 100)
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
                fill = fillStyle
                stroke = strokeStyle
            }
            svg.text {
                body = text
                x = "${bounds.x}"
                y = "${bounds.y + textHeight}"
                FontDetails.NOTES.setSvgDetails(this)
                this.attributes["text-anchor"] = "left"
                this.attributes["dominant-baseline"] = "central"
            }
        }

        // still do the children, even if we aren't drawn
        super.add(diagram, svg, zIndex)
    }

    private var textHeight: Double = 0.0
    override fun prepare(diagram: Diagram, svg: SVG, addedElements: MutableSet<String>, parentOffset: Coord) {
        super.prepare(diagram, svg, addedElements, parentOffset)
        textHeight = diagram.calcHeight(FontDetails.NOTES)
    }
}

fun Container.note(block: (Note.() -> Unit)? = null): Note {
    val note = Note()
    shapes.add(note)
    if (block !== null) {
        note.apply(block)
    }
    return note
}