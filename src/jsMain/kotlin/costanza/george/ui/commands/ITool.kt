package costanza.george.ui.commands

import costanza.george.diagrams.base.Diagram
import costanza.george.geometry.Coord
import costanza.george.reflect.undoredo.Differ

interface ITool {
    var diagram: Diagram?
    fun click(loc: Coord)
}