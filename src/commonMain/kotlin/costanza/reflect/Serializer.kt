package costanza.reflect

/** serialize to a nicely formatted string, mimicking a kotlin dsl */
class Serializer(val registry: EntityTypeRegistry) {
    fun serialize(fnName: String, entity: IEntity, indentLevel: Int = 0): String {
        val bld = StringBuilder()
        var indent = indentLevel
        operator fun StringBuilder.plusAssign(str: String) { this.append(str) }
        fun indent() = (1..indent).forEach { bld += "    " }

        // start by printing out the entityName
        bld += entity.entityName
        indent++

        // handle constructor parameters
        var first = true
        entity.properties.forEach {
            if (it.isConstructor() && !it.isDefault()) {
                bld += if (first) { "(" } else {", "}
                first = false
                bld += it.get()
            }
        }
        if (!first) {
            bld += ")"
        }

        bld += " {\n"

        // handle standard properties
        entity.properties.forEach {
            if (!it.isConstructor()) {
                if (it.isEntity()) {
                    if (it.entity() != null) {
                        indent(); bld += serialize(it.name, it.entity()!!, indent)
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
        indent(); bld += "}\n"
        return bld.toString()
    }
}