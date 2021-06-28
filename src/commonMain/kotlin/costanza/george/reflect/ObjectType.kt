package costanza.george.reflect

/** the creator for making an entity type */
class ObjectType(
    val creator: () -> IObject) {
    val name = creator().reflectInfo().objectType
}

