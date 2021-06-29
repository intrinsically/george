package costanza.george.reflect

class ObjectTypeRegistry {
    private val map = mutableMapOf<String, ObjectType>()

    fun addAll(list: List<ObjectType>) = list.forEach { add(it) }

    fun add(et: ObjectType) {
        if (map.containsKey(et.name)) {
            throw Exception("Entity type $et.entityType already registered")
        }
        map[et.name] = et
    }

    fun create(name: String) =
        (map[name] ?: throw Exception("Entity type $name not found")).creator()
}