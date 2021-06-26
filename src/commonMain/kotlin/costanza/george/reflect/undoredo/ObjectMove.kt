package costanza.george.reflect.undoredo

import costanza.george.diagrams.base.Diagram
import costanza.george.reflect.IObject
import costanza.george.reflect.ObjectTypeRegistry

/** move an entity between 2 parents */
class ObjectMove(
    fromEntity: IObject,
    val fromPropName: String?,
    entity: IObject,
    val from: Int,
    toEntity: IObject,
    val toPropName: String?,
    val to: Int,
) : IChange {
    val fromEntityId = fromEntity.reflectInfo().id!!
    val toEntityId = toEntity.reflectInfo().id!!
    val entityId = entity.reflectInfo().id!!

    override fun undo(registry: ObjectTypeRegistry, diagram: Diagram) {
        val entity = ChangeUtilities.removeObjectFromDiagram(diagram, entityId, true)!!
        ChangeUtilities.findPropertyList(diagram, fromEntityId, fromPropName).list.add(from, entity)
    }

    override fun redo(registry: ObjectTypeRegistry, diagram: Diagram) {
        val entity = ChangeUtilities.removeObjectFromDiagram(diagram, entityId, true)!!
        ChangeUtilities.findPropertyList(diagram, toEntityId, toPropName).list.add(to, entity)
    }

    override fun toString() =
        "ObjectMove(move $entityId from parent $fromEntityId to $toEntityId)"
}