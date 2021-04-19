package costanza.reflect

interface IPrimitiveProperty {
    val name: String
    fun isConstructor(): Boolean
    fun isDefault(): Boolean

    /** getting for serialialization */
    fun get(): String

    /** setting for deserialization */
    fun set(prov: IProvider)
}