package costanza.george.ui.commands

import costanza.george.diagrams.base.Diagram
import costanza.george.geometry.Coord

interface ITool {
    var diagram: Diagram?
    fun click(loc: Coord)
}