package costanza.george.geometry

import costanza.george.reflect.IReflect
import costanza.george.reflect.ReflectInfo
import costanza.george.reflect.entityList
import costanza.george.reflect.reflect
import costanza.george.reflect.typedproperties.double
import kotlin.math.sqrt

data class Coord(var x: Double, var y: Double): IReflect {
    constructor(x: Int, y: Int): this(x.toDouble(), y.toDouble())

    override fun reflectInfo(): ReflectInfo =
        reflect("coord") {
            double("x", false, 0.0, { x }, { x = it })
            double("y", false, 0.0, { y }, { y = it })
        }

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

