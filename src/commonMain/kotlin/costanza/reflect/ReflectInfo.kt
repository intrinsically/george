package costanza.reflect

import costanza.utility._List
import costanza.utility._list

class ReflectInfo(val entityType: String) {
    val properties: _List<PrimitiveProperty> = _list()
    val entities: _List<EntityProperty> = _list()
    val entityLists: _List<EntityListProperty<IReflect>> = _list()
}

/** dsl */
fun reflect(entityType: String, parent: ReflectInfo? = null, block: ReflectInfo.() -> Unit): ReflectInfo {
    val info = ReflectInfo(entityType)
    info.apply(block)
    if (parent != null) {
        info.properties.addAll(parent.properties)
        info.entities.addAll(parent.entities)
        info.entityLists.addAll(parent.entityLists)
    }
    return info
}
