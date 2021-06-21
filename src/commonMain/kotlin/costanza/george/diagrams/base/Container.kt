package costanza.george.diagrams.base

import ksvg.elements.SVG
import costanza.george.geometry.Coord
import costanza.george.geometry.Dim
import costanza.george.geometry.Rect
import costanza.george.reflect.ObjectListProperty
import costanza.george.reflect.ReflectInfo
import costanza.george.reflect.entityList
import costanza.george.reflect.reflect
import costanza.george.reflect.typedproperties.coord
import costanza.george.reflect.typedproperties.dim

abstract class Container: Box() {
    override fun entityType() = "container"

    val shapes: MutableList<Shape> = mutableListOf()
    val prop_shapes = ObjectListProperty(this, null, shapes)
    protected val PADDING = 5.0
    protected var parentOffset = Coord(0,0)

    override fun bounds(diagram: Diagram): Rect {
        // children already include the parent offset
        val childs = boundsOfChildren(diagram)
        if (childs === null) {
            return Rect(bounds.loc, bounds.dim)
        }
        return Rect(bounds.loc, Coord(childs.x2 - parentOffset.x, childs.y2 - parentOffset.y))
                .enforceMinDimensions(bounds.dim)
                .pad(Dim(0,0), Dim(PADDING*2, PADDING*2)) + parentOffset
    }

    /** get the bounds as a collection of the underlying bounds */
    fun boundsOfChildren(diagram: Diagram): Rect? {
        var start: Rect? = null
        shapes.forEach {
            val bounds = it.bounds(diagram)
            start = if (start === null) bounds else start!! + bounds
        }
        return start
    }

    /** add an element to the container */
    override fun add(diagram: Diagram, svg: SVG, zIndex: Int) {
        shapes.forEach {
            it.add(diagram, svg, zIndex)
        }
    }

    override fun determineZIndex(): Int = 0

    override fun collectZIndices(orders: MutableSet<Int>) {
        super.collectZIndices(orders)
        shapes.forEach { it.collectZIndices(orders) }
    }

    override fun prepare(diagram: Diagram, svg: SVG, addedElements: MutableSet<String>, parentOffset: Coord) {
        this.parentOffset = parentOffset
        val offset = parentOffset + Dim(bounds.loc.x, bounds.loc.y)
        shapes.forEach {
            it.prepare(diagram, svg, addedElements, offset)
        }
    }

    /** find a top level shape by id */
    override fun findShape(nameOrId: String): Shape? {
        shapes.forEach {
            val found = it.findShape(nameOrId)
            if (found !== null) {
                return found
            }
        }
        return null
    }

    /** ask each child in turn before checking yourself */
    override fun locateByCoords(d: Diagram, c: Coord, zIndex: Int): Shape? {
        shapes.forEach {
            val loc = it.locateByCoords(d, c, zIndex)
            if (loc != null) {
                return loc
            }
        }
        if (bounds(d).contains(c) && this.determineZIndex() == zIndex) {
            return this
        }
        return null
    }
}