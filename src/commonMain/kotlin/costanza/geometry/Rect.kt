package costanza.geometry

import kotlin.math.max
import kotlin.math.min

data class Rect(val pos: Coord, val dim: Dim) {
    /** construct from an unrolled point and dimension */
    constructor(x1: Int, y1: Int, width: Int, height: Int): this(Coord(x1.toDouble(), y1.toDouble()), Dim(width.toDouble(), height.toDouble()))
    constructor(x1: Double, y1: Double, width: Double, height: Double): this(Coord(x1, y1), Dim(width, height))
    constructor(a: Coord, b: Coord):
            this(min(a.x, b.x), min(a.y, b.y), max(a.x, b.x) - min(a.x, b.x), max(b.y, b.y) - min(a.y, b.y))

    /** ease of use getters */
    val x: Double inline get() = pos.x
    val y: Double inline get() = pos.y
    val x1: Double inline get() = pos.x
    val y1: Double inline get() = pos.y
    val x2: Double inline get() = pos.x + width
    val y2: Double inline get() = pos.y + height
    val width: Double inline get() = dim.width
    val height: Double inline get() = dim.height

    /** return the center coord */
    val center
        get() = Coord(pos.x + dim.width / 2, pos.y + dim.height / 2)

    /** return the mid points of the sides of the rec, starting from top going clockwise */
    fun midSides() =
            arrayOf(pos + Dim(width / 2, 0.0),
                    pos + Dim(width, height / 2),
                    pos + Dim(width / 2, height),
                    pos + Dim(0.0, height / 2))

    /** return the middle of the closes side to the coordinate */
    fun nearestMidSide(c: Coord): Coord {
        return midSides()[nearestSideIndex(c)]
    }

    /** return the index of the side closest to the coord, starting from 0 for top and going clockwise */
    fun nearestSideIndex(c: Coord): Int {
        return midSides().withIndex().minByOrNull { (_, mid) -> mid.distance(c) }?.index!!
    }

    /** order the range correctly */
    private fun range(a: Double, b: Double) = if (a < b) a..b else b..a

    /** is this point able to be made square? */
    fun canBeSquare(c: Coord): Int? {
        // square to side
        when {
            inTop(c) -> return 0
            inRight(c) -> return 1
            inBottom(c) -> return 2
            inLeft(c) -> return 3
        }
        return null
    }

    /** intersect with rectangle, try to keep square if at all possible */
    fun intersect(c: Coord): Coord {
        // square to side
        when (canBeSquare(c)){
            0 -> return Coord(c.x, y1)
            1 -> return Coord(x2, c.y)
            2 -> return Coord(c.x, y2)
            3 -> return Coord(x1, c.y)
        }

        // if inside, pick nearest midpoint
        if (contains(c)) {
            return nearestMidSide(c)
        }

        // check intersections with sides
        val mid = center
        val top = intersectSide(0, c)
        if (top.x in x1..x2 && top.y in range(mid.y, c.y)) {
            return top
        }
        val right = intersectSide(1, c)
        if (right.y in y1..y2 && right.x in range(mid.x, c.x)) {
            return right
        }
        val bottom = intersectSide(2, c)
        if (bottom.x in x1..x2 && bottom.y in range(mid.y, c.y)) {
            return bottom
        }
        return intersectSide(3, c)
    }

    private fun intersectSide(side: Int, c: Coord): Coord {
        // line y = mx + b -> use this to intersect with rect kibe
        val m = (c.y - center.y) / (c.x - center.x)
        val b = c.y - m * c.x
        return when (side) {
            0 -> Coord((y1 - b) / m, y1)
            1 -> Coord(x2, m * x2 + b)
            2 -> Coord((y2 - b) / m, y2)
            else -> Coord(x1, m * x1 + b)
        }
    }

    /** which quadrant of the rectangle does the point lie in */
    fun inTop(c: Coord) = c.x in x1..x2 && c.y <= y1 + 1
    fun inLeft(c: Coord) = c.y in y1..y2 && c.x <= x1 + 1
    fun inRight(c: Coord) = c.y in y1..y2 && c.x >= x2 - 1
    fun inBottom(c: Coord) = c.x in x1..x2 && c.y >= y2 - 1

    /** is the point inside? */
    fun contains(c: Coord) =
            c.x > x1 && c.x < x2 && c.y > y1 && c.y < y2

    /** adding 2 rects makes the union of them */
    operator fun plus(r: Rect) = Rect(Coord(min(x, r.x), min(y, r.y)), Coord(max(x2, r.x2), max(y2, r.y2)))
    /** adding a coord to a rect adds to the origin */
    operator fun plus(c: Coord) = Rect(c.x + x, c.y + y, width, height)
    /** padding out the rect */
    fun pad(padding: Double): Rect = Rect(x1 - padding, y1 - padding, width + padding * 2, height + padding * 2)
    fun pad(a: Dim, b: Dim): Rect = Rect(x1 - a.width, y1 - a.height, width + a.width + b.width, height + a.height + b.height)
    fun enforceMinDimensions(min: Dim): Rect = Rect(x, y, max(min.width, width), max(min.height, height))
}

