package costanza.george.reflect

class ObjectProperty(
    ri: ReflectInfo,
    val name: String,
    val get: () -> IObject?,
    val set: (entity: IObject?) -> Unit
) {
    init {
        ri.objects += this
    }
}

fun ReflectInfo.entity(
    fnName: String,
    get: () -> IObject?,
    set: (entity: IObject?) -> Unit) = objects.add(ObjectProperty(this, fnName, get, set))
