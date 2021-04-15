package costanza.reflect

class EntityTypeRegistry {
    private val map = mutableMapOf<String, EntityType>()

    fun add(et: EntityType) {
        if (map.containsKey(et.name)) {
            throw Exception("Entity type $et.name already registered")
        }
        map[et.name] = et
    }

    fun create(name: String) =
        (map[name] ?: throw Exception("Entity type $name not found")).creator()
}