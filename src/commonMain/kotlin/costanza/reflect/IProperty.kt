package costanza.reflect

interface IProperty {
    fun name(): String
    fun get(): String
    fun put(s: String)
}