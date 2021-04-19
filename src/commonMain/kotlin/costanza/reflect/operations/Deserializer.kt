package costanza.reflect.operations

import costanza.reflect.EntityTypeRegistry
import costanza.reflect.IEntity
import costanza.reflect.IProvider
import costanza.reflect.TokenProvider

class Deserializer(val registry: EntityTypeRegistry) {

    fun deserialize(entityName: String, prov: TokenProvider) =
        deserializeEntity(entityName, entityName, prov)

    private fun deserializeEntity(fnName: String?, entityName: String, prov: TokenProvider): IEntity {

        val entity = registry.create(entityName)
        if (fnName != null) {
            prov.popName(fnName)
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
                val prop = findProperty(entity, name, prov)
                if (prop != null) {
                    prov.popChar('=')
                    prop.set(prov)
                } else {
                    val ent = findEntity(entity, name, prov)
                    if (ent != null) {
                        val sub = deserializeEntity(null, ent.entityType(), prov)
                        ent.set(sub)
                    } else {
                        throw Exception("Cannot find property $name")
                    }
                }
            } while (true)
        }

        return entity
    }

    private fun findProperty(entity: IEntity, name: String, prov: IProvider) = entity.properties.find { it.name == name }
    private fun findEntity(entity: IEntity, fnName: String, prov: IProvider) = entity.entities.find { it.fnName == fnName }
}