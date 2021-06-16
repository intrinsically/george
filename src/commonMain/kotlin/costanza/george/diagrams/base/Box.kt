package costanza.george.diagrams.base

import costanza.george.geometry.Coord
import costanza.george.geometry.Dim
import costanza.george.reflect.ReflectInfo
import costanza.george.reflect.entityList
import costanza.george.reflect.reflect
import costanza.george.reflect.typedproperties.coord
import costanza.george.reflect.typedproperties.dim

abstract class Box(var loc: Coord = Coord(0,0), var dim: Dim = Dim(0,0)): Shape() {
    override fun reflectInfo(): ReflectInfo =
        reflect("box", super.reflectInfo()) {
            coord("loc", false, Coord(0,0), { loc }, { loc = it })
            dim("dim", false, Dim(0,0), { dim }, { dim = it })
        }
}