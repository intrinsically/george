package costanza.george.reflect

/** the creator for making an entity type */
class ObjectType(
    val name: String,
    val creator: () -> IObject)

