package costanza.george.reflect

import costanza.george.utility._List
import costanza.george.utility._list

open class ReflectInfo(val entityType: String) {
    val properties: _List<PrimitiveProperty> = _list()
    val objects: _List<ObjectProperty> = _list()
    val objectLists: _List<ObjectListProperty<IObject>> = _list()
}

/** dsl */
fun reflect(objectType: String, parent: ReflectInfo? = null, block: ReflectInfo.() -> Unit): ReflectInfo {

    fun <T> addAtStart(a: _List<T>, b: _List<T>) {
        val c = _list<T>()
        c.addAll(a)
        a.clear()
        a.addAll(b)
        a.addAll(c)
    }

    val info = ReflectInfo(objectType)
    info.apply(block)
    if (parent != null) {
        addAtStart(info.properties, parent.properties)
        addAtStart(info.objects, parent.objects)
        addAtStart(info.objectLists, parent.objectLists)
    }
    return info
}
