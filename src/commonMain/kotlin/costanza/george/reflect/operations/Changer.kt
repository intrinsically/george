package costanza.george.reflect.operations

import costanza.george.reflect.EntityTypeRegistry
import costanza.george.reflect.IReflect
import costanza.george.reflect.changes.IChange
import costanza.george.utility._List
import costanza.george.utility._list
import costanza.george.utility.loop


class Changer(val top: IReflect, private val registry: EntityTypeRegistry) {
    private val changes = _list<GroupChange>()
    private var current = GroupChange()
    var pos = 0

    /** group a set of changes */
    class GroupChange(val changes:_List<IChange> = _list()) {
        fun back() = changes.reversed().forEach { it.back() }
        fun fwd() = changes.forEach { it.fwd() }
    }

    fun makeChange(change: IChange) {
        current.changes.add(change)
        change.fwd()
    }

    /** mark the transaction */
    fun mark() {
        // if we are in the middle, truncate
        (pos until changes.size).loop { changes.removeLast() }
        changes.add(current)
        current = GroupChange()
        pos++
    }

    fun back() {
        if (pos > 0) {
            changes[--pos].back()
        }
    }

    fun fwd() {
        if (pos < changes.size) {
            changes[pos++].fwd()
        }
    }
}