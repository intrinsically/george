package costanza.reflect.operations.changes

import costanza.reflect.EntityTypeRegistry
import costanza.reflect.IEntity
import costanza.reflect.ITopEntity
import costanza.reflect.TokenProvider
import costanza.reflect.operations.Deserializer
import costanza.reflect.operations.Serializer
import costanza.reflect.operations.findEntityProperty

class EntityChange(
    val top: ITopEntity,
    val entityId: String,
    val propName: String,
    val registry: EntityTypeRegistry,
    entity: IEntity?): IChange {
        var old: String? = null
        var new: String? = null

        init {
            // turn the entities into strings
            val ent = top.find(entityId) ?: throw Exception("Cannot find entity $entityId")
            val prop = findEntityProperty(ent, propName) ?: throw Exception("Cannot find property $propName")
            val serial = Serializer()
            val actual = prop.get()
            old = if (actual != null) { serial.serialize(actual) } else { null }
            new = if (entity != null) { serial.serialize(entity) } else { null }
            fwd()
        }

        override fun back() {
            doit(old)
        }

        override fun fwd() {
            doit(new)
        }

        private fun doit(nextVal: String?) {
            val ent = top.find(entityId) ?: throw Exception("Cannot find entity $entityId")
            val prop = findEntityProperty(ent, propName) ?: throw Exception("Cannot find property $propName")
            if (nextVal == null) {
                prop.set(null)
            } else {
                val deserial = Deserializer(registry)
                val prov = TokenProvider(nextVal)
                prov.popName()
                prop.set(deserial.deserializeEntity(top, prop.entityType, prov))
            }
        }
}