package costanza.reflect.typedproperties

import costanza.reflect.IEntity
import costanza.reflect.IProperty
import costanza.reflect.IProvider

class EntityProperty(
    override val name: String,
    private val myEntityType: String,
    val getter: () -> IEntity?,
    val setter: (prov: IProvider) -> Unit): IProperty {

    override fun isEntity() = true
    override fun entity() = getter()
    override fun set(prov: IProvider) = setter(prov)
}


