package costanza.george.reflect.undoredo

import costanza.george.diagrams.base.Diagram
import costanza.george.reflect.IReflect
import costanza.george.reflect.ObjectListProperty
import costanza.george.reflect.ObjectProperty
import costanza.george.reflect.PrimitiveProperty

/** utility functions that throw an exception if anything goes wrong, so we can handle elegantly
 *  - this is really important in collaborative undo/redo and the objects may haven been deleted by another user
 */

/** error can happen in a collaborative seetting */
class ChangeExpectedException(message: String) : Exception(message)

/** error is unrelated to collaborative undo/redo - it's an actual error */
class ChangeErrorException(message: String) : Exception(message)

class ChangeUtilities {
    companion object {
        /** find the property of an entity */
        fun findProperty(diagram: Diagram, id: String, propName: String): PrimitiveProperty {
            val shape = findShape(diagram, id)
            return shape.findPrimitiveProperty(propName)
                ?: throw ChangeErrorException("Cannot find primitive property $propName on shape $id, type ${shape.objectType}")
        }

        /** find the objectproperty of an entity */
        fun findObjectProperty(diagram: Diagram, id: String, propName: String): ObjectProperty {
            val shape = findShape(diagram, id)
            return shape.findObjectProperty(propName)
                ?: throw ChangeErrorException("Cannot find object property $propName on shape $id, type ${shape.objectType}")
        }

        /** find a shape by id */
        fun findShape(diagram: Diagram, id: String) =
            diagram.findShape(id) ?: throw ChangeExpectedException("Cannot locate shape $id")

        /** remove an object from the diagram. complain if you tell it to when it is not found */
        fun removeObjectFromDiagram(
            diagram: Diagram,
            childId: String,
            exceptionOnFail: Boolean = false
        ): IReflect? {
            val ids = IdAssigner().assignAndMap(diagram)
            val saved = ids[childId]
            if (saved == null) {
                if (exceptionOnFail) {
                    throw ChangeExpectedException("Cannot find child $childId anywhere in diagram")
                }
                return null
            }

            val prop = findPropertyList(diagram, saved.parentId, saved.parentList)
            prop.list.removeAll { it.id == childId }

            return saved.entity
        }

        /** find the list property of an entity */
        fun findPropertyList(diagram: Diagram, id: String, listName: String? = null): ObjectListProperty<IReflect> {
            val shape = findShape(diagram, id)
            return shape.findListProperty(listName)
                ?: throw ChangeErrorException("Cannot find list property $listName on shape $id, type ${shape.objectType}")
        }

        /** add the object to the diagram, but only if it is not there already */
        fun addObjectToDiagram(diagram: Diagram, parentId: String, listName: String?, obj: IReflect, index: Int) {
            if (diagram.findShape(obj.id) != null) {
                return
            }
            findPropertyList(diagram, parentId, listName).list.add(index, obj)
        }
    }

}