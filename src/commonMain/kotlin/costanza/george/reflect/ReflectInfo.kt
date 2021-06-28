package costanza.george.reflect

import costanza.george.reflect.typedproperties.OptionalStringProperty
import costanza.george.utility._List
import costanza.george.utility._list

//interface IReflectInfo {
//    var id: String?
//    val properties: _List<PrimitiveProperty>
//    val objects: _List<ObjectProperty>
//    val objectLists: _List<ObjectListProperty<IObject>>
//}

open class ReflectInfo(var objectType: String) {
    /** id is assigned if we need to reference the object by name */
    var id: String? = null
    val properties: _List<PrimitiveProperty> = _list(OptionalStringProperty(null, "id", false, null, {id}) { id = it })
    val objects: _List<ObjectProperty> = _list()
    val objectLists: _List<ObjectListProperty<IObject>> = _list()

    fun findPrimitiveProperty(name: String) =
        properties.find { it.name == name }

    fun findObjectProperty(fnName: String) =
        objects.find { it.name == fnName }

    fun findListProperty(fnName: String?) =
        objectLists.find { it.name == fnName }
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
