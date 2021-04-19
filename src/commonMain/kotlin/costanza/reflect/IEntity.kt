package costanza.reflect

import costanza.utility.list

/** an entity is an object, it has properties which can either be entities or primitives */
interface IEntity {
    val entityName: String
    val properties: List<IPrimitiveProperty>
    val entities: List<IEntityProperty>
}