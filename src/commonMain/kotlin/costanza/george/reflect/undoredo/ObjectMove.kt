package costanza.george.reflect.undoredo

import costanza.george.reflect.IObject
import costanza.george.reflect.operations.findEntityListProperty

data class ObjectMove(
    val fromEntity: IObject,
    val fromPropName: String?,
    val entity: IObject,
    val from: Int,
    val toEntity: IObject,
    val toPropName: String?,
    val to: Int,
) : IChange {
    fun fromProp() = findEntityListProperty(fromEntity, fromPropName)
        ?: throw Exception("Cannot find entity list property $fromPropName")

    fun toProp() =
        findEntityListProperty(toEntity, toPropName) ?: throw Exception("Cannot find entity list property $toPropName")

    val id = entity.reflectInfo().id

    override fun undo() {
        toProp().list.removeAll { it.reflectInfo().id == id }
        fromProp().list.add(from, entity)
    }

    override fun redo() {
        fromProp().list.removeAll { it.reflectInfo().id == id }
        toProp().list.add(to, entity)
    }
}