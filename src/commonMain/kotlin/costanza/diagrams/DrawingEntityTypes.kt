package costanza.diagrams

import costanza.diagrams.art.Circle
import costanza.diagrams.classpalette.*
import costanza.diagrams.infopalette.Area
import costanza.diagrams.infopalette.Note
import costanza.geometry.Coord
import costanza.reflect.EntityType
import costanza.reflect.EntityTypeRegistry
import costanza.utility.list

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