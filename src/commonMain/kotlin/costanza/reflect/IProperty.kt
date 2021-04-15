package costanza.reflect

/** a property can be primitive or an entity */
interface IProperty {
    val name: String
    fun isEntity() = false
    fun isConstructor() = false

    /** for primitives */
    fun isDefault() = false
    fun get(): String = ""

    /** for entities */
    fun entity(): IEntity? = null

    /** for setting both types */
    fun set(prov: IProvider)
}
