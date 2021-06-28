package costanza.george.reflect.undoredo

import costanza.george.diagrams.base.Diagram
import costanza.george.ecs.Entity
import costanza.george.reflect.ObjectListProperty
import costanza.george.reflect.ObjectTypeRegistry
import costanza.george.reflect.typedproperties.StringProperty
import costanza.george.utility._List
import costanza.george.utility._list


/** group a set of changes */
class GroupChange(var clientId: String = ""): IChange, Entity() {
    override val objectType = "groupchange"

    /** how many changes do we have? */
    val size
        get() = changes.size

    val prop_clientId = StringProperty(this, "clientId", false, "", {clientId}, {clientId=it})
    private val changes:_List<IChange> = _list()
    val prop_changes = ObjectListProperty(this, null, changes)

    fun addChange(change: IChange) {
        changes += change
    }

    override fun undo(registry: ObjectTypeRegistry, diagram: Diagram) =
        changes.reversed().forEach { it.undo(registry, diagram) }

    override fun redo(registry: ObjectTypeRegistry, diagram: Diagram) =
        changes.forEach { it.redo(registry, diagram) }
}