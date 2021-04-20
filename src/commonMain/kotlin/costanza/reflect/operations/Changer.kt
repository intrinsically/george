package costanza.reflect.operations

import costanza.reflect.EntityTypeRegistry
import costanza.reflect.IEntity
import costanza.reflect.ITopEntity
import costanza.reflect.operations.changes.EntityChange
import costanza.reflect.operations.changes.GroupChange
import costanza.reflect.operations.changes.PropertyChange
import costanza.utility._list


class Changer(val top: ITopEntity, val registry: EntityTypeRegistry) {
    private val changes = _list<GroupChange>()
    private var current = GroupChange()
    var pos = 0

    fun changeProperty(entityId: String, propName: String, value: String) =
        current.changes.add(PropertyChange(top, entityId, propName, value))

    fun setEntity(entityId: String, propName: String, entity: IEntity?) =
        current.changes.add(EntityChange(top, entityId, propName, registry, entity))

    fun addEntity() {

    }

    fun reorderEntity() {

    }

    fun deleteEntity() {

    }

    /** mark the transaction */
    fun mark() {
        // if we are in the middle, truncate
        (pos until changes.size).forEach { changes.removeLast() }
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