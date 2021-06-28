package costanza.george.reflect.undoredo

import costanza.george.diagrams.base.Diagram
import costanza.george.ecs.Entity
import costanza.george.reflect.IReflect
import costanza.george.reflect.ObjectTypeRegistry
import costanza.george.reflect.typedproperties.IntProperty
import costanza.george.reflect.typedproperties.OptionalStringProperty
import costanza.george.reflect.typedproperties.StringProperty

/** move an entity between 2 parents */
class ObjectMove(
    var fromEntityId: String = "",
    var fromPropName: String? = null,
    var entityId: String = "",
    var from: Int = 0,
    var toEntityId: String = "",
    var toPropName: String? = null,
    var to: Int = 0,
) : IChange, Entity() {
    override val objectType = "objectmove"

    constructor(fromEntity: IReflect, fromPropName: String?, entity: IReflect, from: Int, toEntity: IReflect, toPropName: String?, to: Int):
            this(fromEntity.id, fromPropName, entity.id, from,
                toEntity.id, toPropName, to)

    val prop_fromEntityId = StringProperty(this, "fromEntityId", false, "", {fromEntityId}, {fromEntityId = it})
    val prop_toEntityId = StringProperty(this, "toEntityId", false, "", {toEntityId}, {toEntityId = it})
    val prop_entityId = StringProperty(this, "entityId", false, "", {entityId}, {entityId = it})
    val prop_fromPropName = OptionalStringProperty(this, "fromPropName", false, "", {fromPropName}, {fromPropName = it})
    val prop_toPropName = OptionalStringProperty(this, "toPropName", false, "", {toPropName}, {toPropName = it})
    val prop_from = IntProperty(this, "from", false, 0, {from}, {from = it})
    val prop_to = IntProperty(this, "to", false, 0, {to}, {to = it})

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