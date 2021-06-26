package costanza.george.ui.commands

import costanza.george.capabilities.ICreateBox
import costanza.george.diagrams.base.Container
import costanza.george.diagrams.base.Diagram
import costanza.george.geometry.Coord
import costanza.george.reflect.undoredo.ObjectCreate

class CreateBoxTool(val creator: ICreateBox): ITool {
    override var diagram: Diagram? = null

    override fun click(loc: Coord) {
        // find the container in the diagram to add to
        var container = diagram?.locate(loc)
        if (!(container is Container)) {
            container = diagram
        }
        val shape = creator.create(loc)
        (container as Container).shapes.add(shape)
    }
}