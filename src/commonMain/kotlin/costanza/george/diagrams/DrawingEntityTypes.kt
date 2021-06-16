package costanza.george.diagrams

import costanza.george.diagrams.art.Circle
import costanza.george.diagrams.classpalette.*
import costanza.george.diagrams.infopalette.Area
import costanza.george.diagrams.infopalette.Note
import costanza.george.geometry.Coord
import costanza.george.reflect.EntityType
import costanza.george.reflect.EntityTypeRegistry
import costanza.george.utility.list

var drawingEntityTypes = list(
    EntityType("coord") { Coord(0,0) },
    EntityType("circle") { Circle() },
    EntityType("association") { Association() },
    EntityType("dependency") { Dependency() },
    EntityType("inheritance") { Inheritance() },
    EntityType("class") { Klass() },
    EntityType("attribute") { Attribute() },
    EntityType("operation") { Operation() },
    EntityType("area") { Area() },
    EntityType("note") { Note() },
)