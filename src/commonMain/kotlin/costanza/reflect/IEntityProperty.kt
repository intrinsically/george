package costanza.reflect

interface IEntityProperty {
    val fnName: String
    val entityType: String

    /** getting for serialization */
    fun get(): IEntity?

    /** setting for deserialization */
    fun set(entity: IEntity?) {}
}