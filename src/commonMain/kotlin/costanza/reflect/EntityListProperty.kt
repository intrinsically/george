package costanza.reflect

import costanza.utility._List

class EntityListProperty<T: IReflect>(
    val propName: String,
    val list: _List<T>
)

fun <T: IReflect> ReflectInfo.entityList(
    fnName: String,
    list: _List<T>) = entityLists.add(EntityListProperty<IReflect>(fnName, list as _List<IReflect>))
