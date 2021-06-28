package costanza.george.reflect.undoredo

import costanza.george.diagrams.base.Diagram
import costanza.george.reflect.IReflect
import costanza.george.reflect.ObjectListProperty
import costanza.george.reflect.ObjectTypeRegistry
import costanza.george.reflect.TokenProvider
import costanza.george.reflect.operations.Deserializer
import costanza.george.reflect.operations.Serializer

class Differ(val ida: IdAssigner, val otr: ObjectTypeRegistry, val diagram: Diagram) {
    lateinit var clone: Diagram
    lateinit var before: Map<String, SavedChild>

    init {
        reset()
    }

    fun reset() {
        // clone by serializing and deserializing
        ida.assignAndMap(diagram) // make sure we have ids first!
        val ser = Serializer().serialize(diagram)
        clone = Deserializer(otr).deserialize(TokenProvider(ser))
        // capture the before state
        before = ida.assignAndMap(clone)
    }

    /** call this once all the changes have been made, to determine the diffs */
    fun determineChanges(): GroupChange {
        val after = ida.assignAndMap(diagram)
        val changes = GroupChange(ida.clientSession)
        determine(diagram, after, changes)
        return changes
    }


    private fun determine(top: IReflect, after: Map<String, SavedChild>, changes: GroupChange) {
        handleProperties(top, changes)
        handleLists(top, after, changes)
        top.objectLists.forEach {
            it.list.forEach { determine(it, after, changes) }
        }
    }

    private fun handleProperties(shape: IReflect, changes: GroupChange) {
        // if new, then don't bother, will be covered by add
        val previous = before[shape.id] ?: return
        val iter = previous.entity.properties.iterator()
        shape.properties.forEach {
            val beforeProp = iter.next()
            if (it.get() != beforeProp.get()) {
                changes.addChange(
                    ObjectPropertyChange(shape, it.name, beforeProp.get(), it.get()))
            }
        }
    }

    private fun handleLists(obj: IReflect, after: Map<String, SavedChild>, changes: GroupChange) {
        // we may not have a reciprocal object in the past diagram
        val prevSaved = before[obj.id]
        val iter = prevSaved?.let { it.entity.objectLists.iterator() }
        obj.objectLists.forEach {
            handleList(obj, it, iter?.next(), after, changes)
        }
    }

    private fun handleList(
        obj: IReflect,
        curr: ObjectListProperty<IReflect>,
        prev: ObjectListProperty<IReflect>?,
        after: Map<String, SavedChild>,
        changes: GroupChange
    ) {
        var pindex = 0
        val pls = prev?.list
        var cindex = 0
        val cls = curr.list
        while (cindex < cls.size) {
            // get current and previous id for the same location
            val currId = cls[cindex].id
            var prevId: String? = null
            if (pls != null && pls.size > pindex) {
                prevId = pls[pindex].id
            }

            when {
                // same on both sides - skip
                    currId == prevId -> {
                        pindex++
                        cindex++
                    }
                // new object added
                    before[currId] == null -> {
                        changes.addChange(
                            ObjectCreate(obj, curr.name, cls[cindex], cindex))
                        handleLists(cls[cindex], after, changes)
                        cindex++
                    }
                // old one has been deleted?
                    prevId != null && after[prevId] == null -> {
                        changes.addChange(
                            ObjectDelete(obj, curr.name, pls!![pindex], cindex))
                        pindex++
                    }
                // moving from one list to another
                    else -> {
                        // if the next element is in place, then assume others moved before it
                        // limitation: currently only apply if it moves containers
                        val prevSaved = before[currId]!!
                        if (prevSaved.parentId != obj.id) {
                            changes.addChange(
                                ObjectMove(
                                    after[prevSaved.parentId]!!.entity,
                                    prevSaved.parentList,
                                    cls[cindex],
                                    prevSaved.index,
                                    obj,
                                    curr.name,
                                    cindex))
                        }
                        cindex++
                    }
            }
        }
    }
}