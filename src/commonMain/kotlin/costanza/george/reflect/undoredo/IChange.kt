package costanza.george.reflect.undoredo

import costanza.george.diagrams.base.Diagram
import costanza.george.reflect.ObjectTypeRegistry

interface IChange {
    fun undo(registry: ObjectTypeRegistry, diagram: Diagram)
    fun redo(registry: ObjectTypeRegistry, diagram: Diagram)
}