package costanza.diagrams.base

import ksvg.elements.SVG
import costanza.geometry.Coord
import costanza.geometry.Dim
import costanza.geometry.Rect
import costanza.reflect.ReflectInfo
import costanza.reflect.entityList
import costanza.reflect.reflect
import costanza.reflect.typedproperties.coord
import costanza.reflect.typedproperties.dim

open class Container(): Box() {
    override fun reflectInfo(): ReflectInfo =
        reflect("container", super.reflectInfo()) {
            coord("loc", false, Coord(0,0), { loc }, { loc = it })
            dim("dim", false, Dim(0,0), { dim }, { dim = it })
            /** polymorphic */
            entityList(shapes)
        }
    var loc: Coord = Coord(0,0)
    var dim: Dim = Dim(0,0)

    val shapes: MutableList<Shape> = mutableListOf()
    protected val PADDING = 5.0
    protected var parentOffset = Coord(0,0)

    override fun bounds(diagram: Diagram): Rect {
        // children already include the parent offset
        val childs = boundsOfChildren(diagram)
        if (childs === null) {
            return Rect(loc, dim)
        }
        return Rect(loc, Coord(childs.x2 - parentOffset.x, childs.y2 - parentOffset.y))
                .enforceMinDimensions(dim)
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
        val offset = parentOffset + Dim(loc.x, loc.y)
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