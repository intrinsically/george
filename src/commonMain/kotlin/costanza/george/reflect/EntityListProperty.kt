package costanza.george.reflect

import costanza.george.utility._List

class EntityListProperty<T: IReflect>(
    val name: String?,
    val list: _List<T>
)

fun <T: IReflect> ReflectInfo.entityList(
    /**
     * if there is no fnName, then we assume any unknown tag is a polymorphic entry
     * you can only have 1 polymorphic entity list per entity
     */
    fnName: String,
    list: _List<T>) = entityLists.add(EntityListProperty(fnName, list as _List<IReflect>))

fun <T: IReflect> ReflectInfo.entityList(
    /**
     * if there is no fnName, then we assume any unknown tag is a polymorphic entry
     * you can only have 1 polymorphic entity list per entity
     */
    list: _List<T>) = entityLists.add(EntityListProperty(null, list as _List<IReflect>))
