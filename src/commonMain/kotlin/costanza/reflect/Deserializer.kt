package costanza.reflect

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
                val prop = findProperty(prov, entity, name)
                if (prop.isEntity()) {
                    val sub = deserializeEntity(null, prop.entityType(), prov)
                    prop.set(sub)
                } else {
                    prov.popChar('=')
                    prop.set(prov)
                }
            } while (true)
        }

        return entity
    }

    private fun findProperty(prov: IProvider, entity: IEntity, name: String) =
        entity.properties.find { it.name == name } ?:
            throw Exception("Cannot find property $name at ($prov.row, $prov.col)")
}