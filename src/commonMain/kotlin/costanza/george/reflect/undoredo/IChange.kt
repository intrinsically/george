package costanza.george.reflect.undoredo

import costanza.george.diagrams.base.Diagram
import costanza.george.reflect.IObject
import costanza.george.reflect.ObjectTypeRegistry

// id as String not String?
// deletegate IObj so it acts ike reflectInfo()

interface IChange: IObject {
    fun undo(registry: ObjectTypeRegistry, diagram: Diagram)
    fun redo(registry: ObjectTypeRegistry, diagram: Diagram)
}