package costanza.george.reflect.undoredo

import costanza.george.diagrams.base.Diagram
import costanza.george.reflect.ObjectTypeRegistry
import costanza.george.utility._List
import costanza.george.utility._list
import costanza.george.utility.loop


class Changer(val clientId: String, private val registry: ObjectTypeRegistry, val diagram: Diagram) {
    private val changes = _list<GroupChange>()
    private var current = GroupChange()
    var pos = 0


    fun recordChange(change: IChange) {
        current.addChange(change)
    }

    fun recordChanges(changes: List<IChange>) {
        changes.forEach { current.addChange(it) }
    }

    /** replace the entire set of changes for this transaction */
    fun recordChanges(group: GroupChange) {
        current = group
    }

    /** only apply these changes if we didn't send them! they don't add to the undo list */
    fun applyCollaborativeChanges(changes: GroupChange, forward: Boolean) {
        if (changes.clientId != clientId) {
            if (forward) {
                changes.redo(registry, diagram)
            } else {
                changes.undo(registry, diagram)
            }
        }
    }

    /** mark the transaction */
    fun markTransaction() {
        // if we are in the middle, truncate
        (changes.size - pos).loop { changes.removeLast() }
        changes.add(current)
        current = GroupChange()
        pos++
    }

    fun undo(): GroupChange? {
        if (canUndo()) {
            val change = changes[--pos]
            change.undo(registry, diagram)
            return change
        }
        return null
    }

    fun redo(): GroupChange? {
        if (canRedo()) {
            val change = changes[pos++]
            change.redo(registry, diagram)
            return change
        }
        return null
    }

    fun canUndo() = pos > 0
    fun canRedo() = pos < changes.size
}