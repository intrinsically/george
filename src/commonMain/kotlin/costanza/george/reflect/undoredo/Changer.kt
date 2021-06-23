package costanza.george.reflect.undoredo

import costanza.george.reflect.ObjectTypeRegistry
import costanza.george.reflect.IObject
import costanza.george.utility._List
import costanza.george.utility._list
import costanza.george.utility.loop


class Changer(val top: IObject, private val registry: ObjectTypeRegistry) {
    private val changes = _list<GroupChange>()
    private var current = GroupChange()
    var pos = 0

    /** group a set of changes */
    class GroupChange(val changes:_List<IChange> = _list()) {
        fun undo() = changes.reversed().forEach { it.undo() }
        fun redo() = changes.forEach { it.redo() }
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
        (pos until changes.size).loop { changes.removeLast() }
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