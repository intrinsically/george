package costanza.diagrams.base

import ksvg.elements.SVG
import costanza.geometry.Coord
import costanza.geometry.Rect
import costanza.reflect.IReflect
import costanza.reflect.ReflectInfo
import costanza.reflect.reflect
import diagrams.base.Diagram
import costanza.reflect.typedproperties.*

abstract class Shape: IReflect {
    override fun reflectInfo(): ReflectInfo =
        reflect("shape") {
            optionalString("id", false, { id }, { id = it })
        }

    /** use name if item clearly is named on the screen. e.g. service name */
    var name: String? = null

    /** unique id - use if name is duplicate etc */
    var id: String? = null

    /** get the shape type, allowing this to be overridden */
    open fun type() = this::class.simpleName ?: ""

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

    /** collect zIndexes */
    open fun collectZIndices(orders: MutableSet<Int>) {
        orders.add(determineZIndex())
    }

    /** the parts for this shape */
    open fun collectParts(): MutableList<Part>? = null

    /** locate a shape with coordinates */
    open fun locateByCoords(d: Diagram, c: Coord, zIndex: Int): Shape? =
        if (determineZIndex() == zIndex && bounds(d).contains(c)) { this } else { null }
}