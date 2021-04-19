package costanza.reflect.operations

import costanza.reflect.EntityTypeRegistry
import costanza.reflect.IEntity
import costanza.utility.loop

/** serialize to a nicely formatted string, mimicking a kotlin dsl */
class Serializer(val registry: EntityTypeRegistry) {

    /** serialize an entity */
    fun serialize(entity: IEntity) = serializeEntity(entity.entityName, entity, 0)

    private fun serializeEntity(fnName: String, entity: IEntity, indentLevel: Int): String {
        val bld = StringBuilder()
        var indent = indentLevel
        operator fun StringBuilder.plusAssign(str: String) {
            this.append(str)
        }
        fun indent() = indent.loop { bld += "    " }

        // start by printing out the entityName
        bld += fnName
        indent++

        // handle constructor parameters
        val cons = entity.properties.filter { it.isConstructor() }.joinToString {
            it.get()
        }
        if (cons.isNotBlank()) { bld += "($cons)" }

        // only add the block if we have actual properties
        val prims = entity.properties.filter { !it.isConstructor() && !it.isDefault() }
        val entities = entity.entities
        if (prims.isNotEmpty() || entities.isNotEmpty()) {
            // handle properties
            bld += " {\n"
            prims.forEach {
                indent(); bld += "${it.name} = ${it.get()}\n"
            }
            entities.filter { it.get() != null }.forEach {
                indent(); bld += serializeEntity(it.fnName, it.get()!!, indent)
            }
            indent--
            indent(); bld += "}"
        }
        bld += "\n"
        return bld.toString()
    }
}