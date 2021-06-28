package costanza.george.diagrams

import costanza.george.diagrams.art.Circle
import costanza.george.diagrams.base.Diagram
import costanza.george.diagrams.classpalette.*
import costanza.george.diagrams.infopalette.Area
import costanza.george.diagrams.infopalette.Note
import costanza.george.geometry.Coord
import costanza.george.reflect.ObjectType
import costanza.george.reflect.undoredo.ObjectCreate
import costanza.george.reflect.undoredo.ObjectDelete
import costanza.george.reflect.undoredo.ObjectMove
import costanza.george.reflect.undoredo.ObjectPropertyChange
import costanza.george.utility.list

var drawingEntityTypes = list(
    ObjectType { Diagram() },
    ObjectType { Coord() },
    ObjectType { Circle() },
    ObjectType { Association() },
    ObjectType { Dependency() },
    ObjectType { Inheritance() },
    ObjectType { Klass() },
    ObjectType { Attribute() },
    ObjectType { Operation() },
    ObjectType { Area() },
    ObjectType { Note() },

    /** change commands */
    ObjectType { ObjectCreate() },
    ObjectType { ObjectDelete() },
    ObjectType { ObjectPropertyChange() },
    ObjectType { ObjectMove() }
)