package costanza.george.reflect.undoredo

import costanza.george.diagrams.base.Container
import costanza.george.diagrams.base.Diagram
import costanza.george.diagrams.base.Shape
import costanza.george.reflect.IObject
import costanza.george.reflect.ObjectTypeRegistry
import costanza.george.reflect.TokenProvider
import costanza.george.reflect.operations.Deserializer
import costanza.george.reflect.operations.Serializer
import costanza.george.utility._List
import costanza.george.utility._list

class Differ(val ida: IdAssigner, val otr: ObjectTypeRegistry, val diagram: Diagram) {
    val clone: Diagram
    var before: Map<String, SavedChild>

    init {
        // clone by serializing and deserializing
        val ser = Serializer().serialize(diagram)
        clone = Diagram()
        Deserializer(otr).deserialize(
            clone,
            TokenProvider(ser)
        )

        // capture the before state
        before = ida.assignAndMap(clone)
    }

    /** call this once all the changes have been made, to determine the diffs */
    fun determineChanges(): List<IChange> {
        val after = ida.assignAndMap(diagram)
        val changes = _list<IChange>()
        determine(diagram, after, changes)
        return changes
    }

    private fun determine(top: IObject, after: Map<String, SavedChild>, changes: _List<IChange>) {
        handleProperties(top, changes)
        handleLists(top, after, changes)
        top.reflectInfo().objectLists.forEach {
            it.list.forEachIndexed { index, obj -> determine(obj, after, changes) }
        }
    }

    private fun handleProperties(shape: IObject, changes: _List<IChange>) {
        // if new, then don't bother, will be covered by add
        val previous = before[shape.reflectInfo().id] ?: return
        val iter = previous.entity.reflectInfo().properties.iterator()
        shape.reflectInfo().properties.forEach {
            val beforeProp = iter.next()
            if (it.get() != beforeProp.get()) {
                changes += ObjectPropertyChange(shape, it.name, beforeProp.get(), it.get())
            }
        }
    }

    private fun handleLists(top: IObject, after: Map<String, SavedChild>, changes: _List<IChange>) {
        // find the same in the previous version. if it's not there, issue an add
//        val saved = after[shape.id]!!
//        if (!before.containsKey(shape.id)) {
//            changes += ObjectCreate(after[saved.parentId]!!, )
//        }
//
//        val iter =
//        shape.reflectInfo().objectLists.forEach {
//            // if this is new put in a delete
//            val bef =
//        }
    }
}