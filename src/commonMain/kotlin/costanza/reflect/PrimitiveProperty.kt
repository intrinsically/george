package costanza.reflect

abstract class PrimitiveProperty(val name: String, val isConstructor: Boolean) {
    abstract fun isDefault(): Boolean
    /** getting for serialialization */
    abstract fun get(): String
    /** setting for deserialization */
    abstract fun set(prov: IProvider)
}