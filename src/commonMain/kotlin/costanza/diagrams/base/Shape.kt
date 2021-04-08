package diagrams.base

import com.github.nwillc.ksvg.elements.SVG
import costanza.geometry.Coord
import costanza.geometry.Rect
import kotlinx.serialization.Serializable

@Serializable
abstract class Shape {
    /** use name if item clearly is named on the screen. e.g. service name */
    var name: String? = null

    /** unique id - use if name is duplicate etc */
    var id: String? = null

    /** prepare the shape */
    abstract fun prepare(diagram: Diagram, svg: SVG, addedElements: MutableSet<String>, parentOffset: Coord)

    /** get the bounds - always absolute, taking into account the parents offset */
    abstract fun bounds(diagram: Diagram): Rect

    /** find a shape by id - up to each shape to interpret */
    open fun findShape(nameOrId: String): Shape? = null

    /** add the graphics to the svg element */
    abstract fun add(diagram: Diagram, svg: SVG, zIndex: Int)

    /** z index - draw from -ve first to +ve */
    abstract fun determineZIndex(): Int

    /** collect zOrders */
    open fun collectZOrders(orders: MutableSet<Int>) {
        orders.add(determineZIndex())
    }
}