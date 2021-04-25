package costanza.geometry

import kotlin.math.sqrt

data class Coord(val x: Double, val y: Double) {
    constructor(x: Int, y: Int): this(x.toDouble(), y.toDouble())

    /** adding a coord and a dimension */
    operator fun plus(d: Dim): Coord = Coord(x + d.width, y + d.height)

    /** adding two coords */
    operator fun plus(c: Coord): Coord = Coord(x + c.x, y + c.y)

    /** subtracting a coord and a dimension */
    operator fun minus(d: Dim): Coord = Coord(x - d.width, y - d.height)

    /** x squared */
    private fun square(a: Double) = a * a

    /** the scalar distance between 2 coords */
    fun distance(c: Coord): Double = sqrt(square(c.x - x) + square(c.y - y))

    /** midpoint between 2 coords */
    fun mid(c: Coord) = Coord(x + (c.x - x) / 2, y + (c.y - y) / 2)
}

