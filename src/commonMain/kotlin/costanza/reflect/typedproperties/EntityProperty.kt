package costanza.reflect.typedproperties

import costanza.reflect.IEntity
import costanza.reflect.IEntityProperty
import costanza.reflect.IProvider

class EntityProperty(
    override val fnName: String,
    override val entityType: String,
    val getter: () -> IEntity?,
    val setter: (entity: IEntity?) -> Unit): IEntityProperty {

    override fun get() = getter()
    override fun set(entity: IEntity?) = setter(entity)
}


