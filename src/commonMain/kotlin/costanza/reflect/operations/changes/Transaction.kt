package costanza.reflect.operations.changes

import costanza.utility._List
import costanza.utility._list

class GroupChange(val changes:_List<IChange> = _list<IChange>()): IChange {
    override fun back() {
        changes.reversed().forEach { it.back() }
    }

    override fun fwd() {
        changes.forEach { it.fwd() }
    }
}