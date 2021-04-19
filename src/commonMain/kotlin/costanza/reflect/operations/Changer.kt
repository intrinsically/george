package costanza.reflect.operations

import costanza.reflect.EntityTypeRegistry
import costanza.reflect.ITopEntity
import costanza.utility._List
import costanza.utility._list

class Change(id: String, old: String, new: String)
class Transaction(val changes: _List<Change>)

class Changer(top: ITopEntity, val registry: EntityTypeRegistry) {
    val changes = _list<Transaction>()
    var current = Transaction()

    fun changeProperty(id: String) {
    }

    fun addEntity() {

    }

    fun changeEntity() {

    }

    fun reorderEntity() {

    }

    fun deleteEntity() {

    }

    fun storeTransaction() {

    }

    fun undo() {

    }

    fun redo() {

    }
}