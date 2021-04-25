package costanza.reflect.operations

import costanza.reflect.EntityTypeRegistry
import costanza.reflect.IReflect
import costanza.reflect.TokenProvider

fun findPrimitiveProperty(entity: IReflect, name: String) =
    entity.reflectInfo().properties.find { it.name == name }

fun findEntityProperty(entity: IReflect, fnName: String) =
    entity.reflectInfo().entities.find { it.propName == fnName }

fun findEntityListProperty(entity: IReflect, fnName: String?) =
    entity.reflectInfo().entityLists.find { it.propName == fnName }

class Deserializer(val registry: EntityTypeRegistry) {

    fun deserialize(top: IReflect, prov: TokenProvider) =
        deserializeEntity(top, null, prov)

    fun deserializeEntity(top: IReflect, entityType: String?, prov: TokenProvider): IReflect {

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
            entity.reflectInfo().properties.forEach {
                if (it.isConstructor) {
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
                val ent = findEntityProperty(entity, name)
                val entls = findEntityListProperty(entity, name)
                when {
                    prop != null -> {
                        prov.popChar('=')
                        prop.set(prov)
                    }
                    ent != null -> {
                        prov.popChar(':')
                        val sub = deserializeEntity(top, prov.popName(), prov)
                        ent.set(sub)
                    }
                    entls != null -> {
                        prov.popChar(':')
                        val sub = deserializeEntity(top, prov.popName(), prov)
                        entls.list.add(sub)
                    }
                    else -> {
                        // possibly a polymorphic entity
                        var poly = findEntityListProperty(entity, null) ?: throw Exception("Cannot find property $name")
                        val sub = deserializeEntity(top, name, prov)
                        poly.list.add(sub)
                    }
                }
            } while (true)
        }

        return entity
    }
}