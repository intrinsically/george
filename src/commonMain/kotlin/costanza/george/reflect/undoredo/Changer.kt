package costanza.george.reflect.undoredo

import costanza.george.diagrams.base.Diagram
import costanza.george.reflect.ObjectTypeRegistry
import costanza.george.utility._List
import costanza.george.utility._list
import costanza.george.utility.loop


class Changer(val diagram: Diagram, private val registry: ObjectTypeRegistry) {
    private val changes = _list<GroupChange>()
    private var current = GroupChange()
    var pos = 0

    /** group a set of changes */
    inner class GroupChange(val changes:_List<IChange> = _list()) {
        fun undo() = changes.reversed().forEach { it.undo(registry, diagram) }
        fun redo() = changes.forEach { it.redo(registry, diagram) }
    }

    fun recordChange(change: IChange) {
        current.changes += change
    }

    fun recordChanges(changes: List<IChange>) {
        current.changes += changes
    }

    /** mark the transaction */
    fun markTransaction() {
        // if we are in the middle, truncate
        (changes.size - pos).loop { changes.removeLast() }
        changes.add(current)
        current = GroupChange()
        pos++
    }

    fun undo() {
        if (pos > 0) {
            changes[--pos].undo()
        }
    }

    fun redo() {
        if (pos < changes.size) {
            changes[pos++].redo()
        }
    }
}