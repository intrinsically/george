package costanza.reflect

class EntityTypeRegistry {
    private val map = mutableMapOf<String, EntityType>()

    fun addAll(list: List<EntityType>) = list.forEach { addAll(it) }

    fun addAll(et: EntityType) {
        if (map.containsKey(et.name)) {
            throw Exception("Entity type $et.entityType already registered")
        }
        map[et.name] = et
    }

    fun create(name: String) =
        (map[name] ?: throw Exception("Entity type $name not found")).creator()
}