package costanza.reflect

interface IEntityType {
    fun name(): String
    fun create(part: String)
}