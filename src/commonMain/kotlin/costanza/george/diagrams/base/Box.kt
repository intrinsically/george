package costanza.george.diagrams.base

import costanza.george.diagrams.components.CBounds
import costanza.george.geometry.Coord
import costanza.george.geometry.Dim
import costanza.george.reflect.ReflectInfo
import costanza.george.reflect.entityList
import costanza.george.reflect.reflect
import costanza.george.reflect.typedproperties.coord
import costanza.george.reflect.typedproperties.dim

abstract class Box(loc: Coord = Coord(0,0), dim: Dim = Dim(0,0)): Shape() {
    override fun entityType() = "box"

    val bounds = CBounds(this, loc, dim)
}