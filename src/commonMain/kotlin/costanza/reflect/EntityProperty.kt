package costanza.reflect

class EntityProperty(
    val propName: String,
    val get: () -> IReflect?,
    val set: (entity: IReflect?) -> Unit
)

fun ReflectInfo.entity(
    fnName: String,
    get: () -> IReflect?,
    set: (entity: IReflect?) -> Unit) = entities.add(EntityProperty(fnName, get, set))
