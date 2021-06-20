package costanza.george.reflect

class EntityProperty(
    val name: String,
    val get: () -> IReflect?,
    val set: (entity: IReflect?) -> Unit
)

fun ReflectInfo.entity(
    fnName: String,
    get: () -> IReflect?,
    set: (entity: IReflect?) -> Unit) = entities.add(EntityProperty(fnName, get, set))
