package costanza.reflect

/** interchange is always via strings */
interface IProperty {
    val name: String
    fun get(): String
    fun set(s: String)
}
