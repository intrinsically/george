package diagrams.base

import com.github.nwillc.ksvg.elements.SVG
import costanza.diagrams.base.Container
import costanza.diagrams.base.FontDetails
import costanza.diagrams.base.ITextCalculator
import costanza.geometry.Coord
import costanza.geometry.Dim
import costanza.geometry.Rect

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
class Diagram: Container(), ITextCalculator {
    var debug: Boolean = false
    @Transient
    val addedElements = mutableSetOf<String>()
    @Transient
    lateinit var calc: ITextCalculator

    override fun determineZIndex() = 0

    /** simpler add */
    fun add(svg: SVG) {
        prepare(this, svg, addedElements, Coord(0,0))

        // draw in zIndex order
        val orders = mutableSetOf<Int>()
        shapes.forEach {
            it.collectZOrders(orders)
        }
        orders.forEach {
            add(this, svg, it)
        }
    }

    /** get the bounds as a collection of the underlying bounds */
    override fun bounds(diagram: Diagram) =
        (super.boundsOfChildren(diagram) ?: Rect(0,0,0,0)).pad(Dim(0, 0), Dim(PADDING, PADDING))

    /** find a top level box by id (up to shape to interpret, could be name for instance) */
    fun findBox(nameOrId: String): Box? {
        shapes.forEach {
            val found = it.findShape(nameOrId)
            if (found !== null && found is Box) {
                return found
            }
        }
        return null
    }

    override fun calcHeight(details: FontDetails): Double {
        return calc.calcHeight(details)
    }

    override fun calcWidth(details: FontDetails, minWidth: Double, text: String): Double {
        return calc.calcWidth(details, minWidth, text)
    }
}

fun diagram(calc: ITextCalculator, name: String, block: Diagram.() -> Unit): Diagram {
    val diagram = Diagram()
    diagram.name = name
    diagram.calc = calc
    diagram.apply(block)
    return diagram
}
