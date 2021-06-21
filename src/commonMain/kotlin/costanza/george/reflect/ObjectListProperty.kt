package costanza.george.reflect

import costanza.george.utility._List

class ObjectListProperty<T: IObject>(
    ri: ReflectInfo,
    val name: String?,
    val list: _List<T>
) {
    init {
        ri.objectLists += this as ObjectListProperty<IObject>
    }
}

fun <T: IObject> ReflectInfo.entityList(
    /**
     * if there is no fnName, then we assume any unknown tag is a polymorphic entry
     * you can only have 1 polymorphic entity list per entity
     */
    fnName: String,
    list: _List<T>) = objectLists.add(ObjectListProperty(this, fnName, list as _List<IObject>))

fun <T: IObject> ReflectInfo.entityList(
    /**
     * if there is no fnName, then we assume any unknown tag is a polymorphic entry
     * you can only have 1 polymorphic entity list per entity
     */
    list: _List<T>) = objectLists.add(ObjectListProperty(this, null, list as _List<IObject>))
