package costanza.reflect

import utility.loop

/** serialize to a nicely formatted string, mimicking a kotlin dsl */
class Serializer(val registry: EntityTypeRegistry) {

    /** serialize an entity */
    fun serialize(entity: IEntity) = serializeEntity(entity.entityName, entity, 0)

    private fun serializeEntity(fnName: String, entity: IEntity, indentLevel: Int): String {
        val bld = StringBuilder()
        var indent = indentLevel
        operator fun StringBuilder.plusAssign(str: String) { this.append(str) }
        fun indent() = indent.loop { bld += "    " }

        // start by printing out the entityName
        bld += fnName
        indent++

        // handle constructor parameters
        var first = true
        entity.properties.forEach {
            if (it.isConstructor()) {
                bld += if (first) { "(" } else {", "}
                first = false
                bld += it.get()
            }
        }
        if (!first) {
            bld += ")"
        }

        // only add the block if we have actual properties
        if (entity.properties.any { !it.isConstructor() && !it.isDefault() }) {
            // handle standard properties
            first = true
            entity.properties.forEach {
                if (first) {
                    bld += " {\n"
                    first = false
                }

                if (!it.isConstructor()) {
                    if (it.isEntity()) {
                        if (it.entity() != null) {
                            indent(); bld += serializeEntity(it.name, it.entity()!!, indent)
                        }
                    } else {
                        // primitive - don't bother if default value
                        if (!it.isDefault()) {
                            indent(); bld += "${it.name} = " + it.get() + "\n"
                        }
                    }
                }
            }
            indent--
            indent(); bld += "}"
        }
        return bld.toString() + "\n"
    }
}