package costanza.george.diagrams.components

import costanza.george.ecs.Component
import costanza.george.ecs.Entity
import costanza.george.geometry.Coord
import costanza.george.reflect.typedproperties.CoordProperty
import costanza.george.reflect.typedproperties.DoubleProperty

class CBoundsCircle(
    entity: Entity,
    var loc: Coord,
    var radius: Double,
    prefix: String = ""
) : Component(entity) {
    val prop_loc =
        CoordProperty(entity, prefix + "loc", false, Coord(0, 0), { loc }, { loc = it })
    val prop_radius =
        DoubleProperty(entity, prefix + "radius", false, 0.0, { radius }, { radius = it })
}

