package costanza.reflect

interface IPrimitiveProperty {
    val name: String
    fun isConstructor()
    fun isDefault() = false

    /** getting for serialialization */
    fun get(): String

    /** setting for deserialization */
    fun set(prov: IProvider)
}