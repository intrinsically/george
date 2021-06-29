package costanza.george.diagrams

import costanza.george.diagrams.art.Circle
import costanza.george.diagrams.base.Diagram
import costanza.george.diagrams.classpalette.*
import costanza.george.diagrams.infopalette.Area
import costanza.george.diagrams.infopalette.Note
import costanza.george.geometry.Coord
import costanza.george.reflect.ObjectType
import costanza.george.utility.list

var drawingEntityTypes = list(
    ObjectType("diagram") { Diagram() },
    ObjectType("coord") { Coord(0,0) },
    ObjectType("circle") { Circle() },
    ObjectType("association") { Association() },
    ObjectType("dependency") { Dependency() },
    ObjectType("inheritance") { Inheritance() },
    ObjectType("class") { Klass() },
    ObjectType("attribute") { Attribute() },
    ObjectType("operation") { Operation() },
    ObjectType("area") { Area() },
    ObjectType("note") { Note() },
)