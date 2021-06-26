package costanza.george.diagrams.base

import ksvg.elements.SVG
import costanza.george.geometry.Coord
import costanza.george.geometry.Dim
import costanza.george.geometry.Rect
import costanza.george.reflect.ObjectTypeRegistry
import costanza.george.reflect.ReflectInfo
import costanza.george.reflect.operations.Serializer
import costanza.george.reflect.undoredo.Changer
import costanza.george.reflect.reflect
import costanza.george.reflect.undoredo.Differ

class Diagram: Container(), ITextCalculator {
    override fun entityType() = "diagram"

    /** undo-redo list for this diagram & the differ to calculate changes */
    var changer: Changer? = null
    var differ: Differ? = null

    var debug: Boolean = false
    val addedElements = mutableSetOf<String>()
    lateinit var calc: ITextCalculator

    /** record and changes to the diagram as a set of IChange deltas, which can be done/undone */
    fun recordChanges() {
        val diff = differ ?: return
        val change = changer ?: return

        val changes = diff.determineChanges()
        change.recordChanges(changes)
        change.markTransaction()

        // print out the changes
        changes.forEach { println(Serializer().serialize((it))) }

        // reset the differ
        diff.reset()
    }

    fun undo() = changer?.undo()
    fun redo() = changer?.redo()

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
