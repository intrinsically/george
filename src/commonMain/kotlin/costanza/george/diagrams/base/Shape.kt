package costanza.george.diagrams.base

import costanza.george.ecs.Entity
import ksvg.elements.SVG
import costanza.george.geometry.Coord
import costanza.george.geometry.Rect
import costanza.george.reflect.typedproperties.*

abstract class Shape: Entity() {
    override fun entityType() = "shape"

    /** use name if item clearly is named on the screen. e.g. service name */
    var name: String? = null
    var prop_name = OptionalStringProperty(this, "name", true, null, {name}) { name = it }

    /** unique id - use if name is duplicate etc */
    var id: String? = null
    var prop_id = OptionalStringProperty(this, "name", true, null, {name}) { name = it }

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