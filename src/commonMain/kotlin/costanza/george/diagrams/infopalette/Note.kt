package costanza.george.diagrams.infopalette

import costanza.george.diagrams.base.Container
import costanza.george.diagrams.base.Diagram
import costanza.george.diagrams.base.FontDetails
import costanza.george.diagrams.base.LINE_Z_ORDER
import costanza.george.geometry.Coord
import costanza.george.geometry.Dim
import costanza.george.reflect.typedproperties.IntProperty
import costanza.george.reflect.typedproperties.StringProperty
import ksvg.elements.SVG

class Note(var text: String = ""): Container() {
    override val objectType = "note"

    val prop_text = StringProperty(this, "text", false, "", { text }, { text = it })
    var zIndex = LINE_Z_ORDER + 10 // on top of even lines
    val prop_zIndex = IntProperty(this, "zIndex", false, LINE_Z_ORDER + 10, { zIndex }, { zIndex = it })

    /** hardcoded currently */
    val fillStyle: String = "#fffdd0"
    val strokeStyle: String = "gray"

    override fun determineZIndex() = zIndex

    init {
        bounds.dim = Dim(150, 100)
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