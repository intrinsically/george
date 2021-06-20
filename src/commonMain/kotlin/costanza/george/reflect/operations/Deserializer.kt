package costanza.george.reflect.operations

import costanza.george.reflect.ObjectTypeRegistry
import costanza.george.reflect.IObject
import costanza.george.reflect.TokenProvider

fun findPrimitiveProperty(entity: IObject, name: String) =
    entity.reflectInfo().properties.find { it.name == name }

fun findObjectProperty(entity: IObject, fnName: String) =
    entity.reflectInfo().objects.find { it.name == fnName }

fun findEntityListProperty(entity: IObject, fnName: String?) =
    entity.reflectInfo().objectLists.find { it.name == fnName }

class Deserializer(val registry: ObjectTypeRegistry) {

    fun deserialize(top: IObject, prov: TokenProvider) =
        deserializeEntity(top, null, prov)

    fun deserializeEntity(top: IObject, entityType: String?, prov: TokenProvider): IObject {

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
                val ent = findObjectProperty(entity, name)
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