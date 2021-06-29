package costanza.george.components

import costanza.george.ecs.Component
import costanza.george.ecs.Entity
import costanza.george.geometry.Coord
import costanza.george.geometry.Dim
import costanza.george.reflect.typedproperties.CoordProperty
import costanza.george.reflect.typedproperties.DimProperty

class CBounds(
    entity: Entity,
    var loc: Coord,
    var dim: Dim,
    prefix: String = ""
) : Component(entity) {
    val prop_loc =
        CoordProperty(entity, prefix + "loc", false, Coord(0, 0), { loc }, { loc = it })
    val prop_dim =
        DimProperty(entity, prefix + "dim", false, Dim(0, 0), { dim }, { dim = it })
}
