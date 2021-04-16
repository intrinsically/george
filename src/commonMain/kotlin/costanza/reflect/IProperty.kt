package costanza.reflect

/** a property can be primitive or an entity */
interface IProperty {
    val name: String
    fun isEntity() = false
    fun isConstructor() = false

    /** for primitives */
    fun isDefault() = false
    fun get(): String = ""
    fun set(prov: IProvider)

    /** for entities */
    fun entityType(): String = ""
    fun entity(): IEntity? = null
    fun set(entity: IEntity) {}
}
