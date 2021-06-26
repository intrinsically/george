package costanza.george.reflect.operations

import costanza.george.reflect.ObjectTypeRegistry
import costanza.george.reflect.IObject
import costanza.george.reflect.TokenProvider

class Deserializer(val registry: ObjectTypeRegistry) {

    fun <T: IObject> deserialize(prov: TokenProvider) =
        deserializeEntity(prov) as T

    fun deserializeEntity(prov: TokenProvider, entityType: String? = null): IObject {

        // create the top level entity
        val entityType = entityType ?: prov.popName()
        val entity = registry.create(entityType)

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
                val prop = entity.reflectInfo().findPrimitiveProperty(name)
                val ent = entity.reflectInfo().findObjectProperty(name)
                val entls = entity.reflectInfo().findListProperty(name)
                when {
                    prop != null -> {
                        prov.popChar('=')
                        prop.set(prov)
                    }
                    ent != null -> {
                        prov.popChar(':')
                        val sub = deserializeEntity(prov)
                        ent.set(sub)
                    }
                    entls != null -> {
                        prov.popChar(':')
                        val sub = deserializeEntity(prov)
                        entls.list.add(sub)
                    }
                    else -> {
                        // possibly a polymorphic entity
                        var poly = entity.reflectInfo().findListProperty(null) ?: throw Exception("Cannot find property $name")
                        val sub = deserializeEntity(prov, name)
                        poly.list.add(sub)
                    }
                }
            } while (true)
        }

        return entity
    }
}