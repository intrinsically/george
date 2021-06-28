package costanza.george.reflect

/** the creator for making an entity type */
class ObjectType(
    val creator: () -> IReflect) {
    val name = creator().objectType
}

