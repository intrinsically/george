package costanza.diagrams.base

import ksvg.elements.SVG
import costanza.geometry.Coord
import costanza.geometry.Dim
import costanza.geometry.Rect
import costanza.reflect.ReflectInfo
import costanza.reflect.entityList
import costanza.reflect.reflect
import costanza.reflect.typedproperties.double
import diagrams.base.Box
import diagrams.base.Diagram

open class Container(): Box() {
    override fun reflectInfo(): ReflectInfo =
        reflect("container", super.reflectInfo()) {
            double("x", false, 0.0, { x }, { x = it })
            double("y", false, 0.0, { y }, { y = it })
            double("width", false, 0.0, { width }, { width = it })
            double("height", false, 0.0, { height }, { height = it })
            entityList("shapes", shapes)
        }
    var x: Double = 0.0
    var y: Double = 0.0
    var width: Double = 0.0
    var height: Double = 0.0

    val shapes: MutableList<Shape> = mutableListOf()
    protected val PADDING = 5.0
    protected var parentOffset = Coord(0,0)

    override fun bounds(diagram: Diagram): Rect {
        // children already include the parent offset
        val childs = boundsOfChildren(diagram)
        if (childs === null) {
            return Rect(x, y, width, height)
        }
        return Rect(Coord(x, y), Coord(childs.x2 - parentOffset.x, childs.y2 - parentOffset.y))
                .enforceMinDimensions(Dim(width, height))
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
        val offset = parentOffset + Dim(x, y)
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