package costanza.george.reflect

class EntityTypeRegistry {
    private val map = mutableMapOf<String, EntityType>()

    fun addAll(list: List<EntityType>) = list.forEach { add(it) }

    fun add(et: EntityType) {
        if (map.containsKey(et.name)) {
            throw Exception("Entity type $et.entityType already registered")
        }
        map[et.name] = et
    }

    fun create(name: String) =
        (map[name] ?: throw Exception("Entity type $name not found")).creator()
}