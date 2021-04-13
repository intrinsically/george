package costanza.reflect

class EntityTypeRegistry {
    private val map = mutableMapOf<String, IEntityType>()

    fun add(et: IEntityType) {
        map[et.name()] = et
    }
}