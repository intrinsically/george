package costanza.reflect

/** the creator for making an entity type */
class EntityType(
    val name: String,
    val creator: () -> IEntity)

