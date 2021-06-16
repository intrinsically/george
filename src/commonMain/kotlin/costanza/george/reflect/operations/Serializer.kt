package costanza.george.reflect.operations

import costanza.george.reflect.IReflect
import costanza.george.utility.iloop
import costanza.george.utility.loop

/** serialize to a nicely formatted string, mimicking a kotlin dsl */
class Serializer {

    /** serialize an entity */
    fun serialize(entity: IReflect) = serializeEntity(entity.reflectInfo().entityType, entity, 0)

    private fun serializeEntity(fnName: String, entity: IReflect, indentLevel: Int): String {
        val bld = StringBuilder()
        var indent = indentLevel
        operator fun StringBuilder.plusAssign(str: String) {
            this.append(str)
        }

        fun indent() = indent.loop { bld += "    " }

        // start by printing out the entityType
        bld += fnName
        indent++

        // handle constructor parameters
        val cons = entity.reflectInfo().properties.filter { it.isConstructor }.joinToString {
            it.get()
        }
        if (cons.isNotBlank()) {
            bld += "($cons)"
        }

        // only add the block if we have actual properties
        val prims = entity.reflectInfo().properties.filter { !it.isConstructor && !it.isDefault() }
        val entities = entity.reflectInfo().entities.filter { it.get() != null }
        val entityLists = entity.reflectInfo().entityLists.filter { it.list.isNotEmpty() }
        if (prims.isNotEmpty() || entities.isNotEmpty() || entityLists.isNotEmpty()) {
            // handle properties
            bld += " {\n"
            prims.forEach {
                indent(); bld += "${it.name} = ${it.get()}\n"
            }
            entities.filter { it.get() != null }.forEach {
                indent()
                bld += it.propName + ":"
                val elem = it.get()!!
                bld += serializeEntity(elem.reflectInfo().entityType, elem, indent)
            }
            entityLists.forEach {
                it.list.size.iloop { index ->
                    indent()
                    val elem = it.list[index]
                    if (it.propName != null) {
                        bld += it.propName + ":"
                    }
                    bld += serializeEntity(elem.reflectInfo().entityType, elem, indent)
                }
            }
            indent--
            indent(); bld += "}"
        }
        bld += "\n"
        return bld.toString()
    }
}