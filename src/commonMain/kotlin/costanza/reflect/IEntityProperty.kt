package costanza.reflect

interface IEntityProperty {
    val name: String

    fun entityType(): String

    /** getting for serialization */
    fun entity(): IEntity?

    /** setting for deserialization */
    fun set(entity: IEntity) {}
}