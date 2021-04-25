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

    fun <T> addAtStart(a: _List<T>, b: _List<T>) {
        val c = _list<T>()
        c.addAll(a)
        a.clear()
        a.addAll(b)
        a.addAll(c)
    }

    val info = ReflectInfo(entityType)
    info.apply(block)
    if (parent != null) {
        addAtStart(info.properties, parent.properties)
        addAtStart(info.entities, parent.entities)
        addAtStart(info.entityLists, parent.entityLists)
    }
    return info
}
