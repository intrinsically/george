package costanza.george.reflect

class ObjectProperty(
    val name: String,
    val get: () -> IObject?,
    val set: (entity: IObject?) -> Unit
)

fun ReflectInfo.entity(
    fnName: String,
    get: () -> IObject?,
    set: (entity: IObject?) -> Unit) = objects.add(ObjectProperty(fnName, get, set))
