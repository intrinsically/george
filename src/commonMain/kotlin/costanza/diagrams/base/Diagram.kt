package costanza.diagrams.base

import ksvg.elements.SVG
import costanza.geometry.Coord
import costanza.geometry.Dim
import costanza.geometry.Rect
import costanza.reflect.ReflectInfo
import costanza.reflect.reflect

class Diagram: Container(), ITextCalculator {
    override fun reflectInfo(): ReflectInfo =
        reflect("diagram", super.reflectInfo()) {
        }

    var debug: Boolean = false
    val addedElements = mutableSetOf<String>()
    lateinit var calc: ITextCalculator

    /** simpler add */
    fun add(svg: SVG) {
        prepare(this, svg, addedElements, Coord(0,0))
        // draw in zIndex order
        val orders = mutableSetOf<Int>()
        collectZIndices(orders)
        orders.forEach {
            add(this, svg, it)
        }
    }

    /** locate shape via coords */
    fun locate(c: Coord): Shape? {
        // search in reversed index order
        val orders = mutableSetOf<Int>()
        collectZIndices(orders)
        orders.reversed().forEach {
            val loc = locateByCoords(this, c, it)
            if (loc != null) {
                return loc
            }
        }
        return null
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
