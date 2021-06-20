package costanza.george.reflect

import costanza.george.utility._List

class ObjectListProperty<T: IObject>(
    val name: String?,
    val list: _List<T>
)

fun <T: IObject> ReflectInfo.entityList(
    /**
     * if there is no fnName, then we assume any unknown tag is a polymorphic entry
     * you can only have 1 polymorphic entity list per entity
     */
    fnName: String,
    list: _List<T>) = objectLists.add(ObjectListProperty(fnName, list as _List<IObject>))

fun <T: IObject> ReflectInfo.entityList(
    /**
     * if there is no fnName, then we assume any unknown tag is a polymorphic entry
     * you can only have 1 polymorphic entity list per entity
     */
    list: _List<T>) = objectLists.add(ObjectListProperty(null, list as _List<IObject>))
