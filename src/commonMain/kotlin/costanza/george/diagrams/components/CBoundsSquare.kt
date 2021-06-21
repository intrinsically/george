package costanza.george.diagrams.components

import costanza.george.ecs.Component
import costanza.george.ecs.Entity
import costanza.george.geometry.Coord
import costanza.george.reflect.typedproperties.CoordProperty
import costanza.george.reflect.typedproperties.DoubleProperty

class CBoundsSquare(entity: Entity, var loc: Coord, var side: Double, prefix: String = "") : Component(entity) {
    val prop_loc =
        CoordProperty(entity, prefix + "loc", false, Coord(14, 14), { loc }, { loc = it })
    val prop_side =
        DoubleProperty(entity, prefix + "side", false, 10.0, { side }, { side = it })
}
