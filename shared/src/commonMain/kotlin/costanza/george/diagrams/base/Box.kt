package costanza.george.diagrams.base

import costanza.george.components.CBounds
import costanza.george.geometry.Coord
import costanza.george.geometry.Dim

abstract class Box(loc: Coord = Coord(0,0), dim: Dim = Dim(0,0)): Shape() {
    override fun entityType() = "box"

    val bounds = CBounds(this, loc, dim)
}