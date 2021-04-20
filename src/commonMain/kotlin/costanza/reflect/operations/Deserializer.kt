package costanza.reflect.operations

import costanza.reflect.*

fun findPrimitiveProperty(entity: IEntity, name: String) = entity.properties.find { it.name == name }
fun findEntityProperty(entity: IEntity, fnName: String) = entity.entities.find { it.fnName == fnName }

class Deserializer(private val registry: EntityTypeRegistry) {

    fun deserialize(top: ITopEntity, prov: TokenProvider) =
        deserializeEntity(top, null, prov)

    fun deserializeEntity(top: ITopEntity, entityType: String?, prov: TokenProvider): IEntity {

        val entity = if (entityType != null) {
            registry.create(entityType)
        } else {
            prov.popName()
            top
        }

        prov.skip()
        val next = prov.peek()
        if (next == '(') {
            prov.pop()
            // handle constructor params
            var some = false
            entity.properties.forEach {
                if (it.isConstructor()) {
                    if (some) {
                        prov.popChar(',')
                    }
                    it.set(prov)
                    some = true
                }
            }
            prov.popChar(')')
        }

        // handle the block
        prov.skip()
        if (prov.peek() == '{') {
            prov.pop()

            // get the name and see if this is assignment or another entity
            do {
                prov.skip()
                if (prov.peek() == '}') {
                    prov.pop()
                    break
                }
                val name = prov.popName()
                val prop = findPrimitiveProperty(entity, name)
                if (prop != null) {
                    prov.popChar('=')
                    prop.set(prov)
                } else {
                    val ent = findEntityProperty(entity, name)
                    if (ent != null) {
                        val sub = deserializeEntity(top, ent.entityType, prov)
                        ent.set(sub)
                    } else {
                        throw Exception("Cannot find property $name")
                    }
                }
            } while (true)
        }
        if (entity.id == null) {
            entity.id = top.makeId()
        }

        return entity
    }
}