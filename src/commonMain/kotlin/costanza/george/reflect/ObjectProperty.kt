package costanza.george.reflect

class ObjectProperty(
    ri: ReflectBase,
    val name: String,
    val get: () -> IReflect?,
    val set: (entity: IReflect?) -> Unit
) {
    init {
        ri.objects += this
    }
}

fun ReflectBase.entity(
    fnName: String,
    get: () -> IReflect?,
    set: (entity: IReflect?) -> Unit) = objects.add(ObjectProperty(this, fnName, get, set))
