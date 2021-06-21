package costanza.george.reflect.operations

import costanza.george.reflect.IObject
import costanza.george.utility.iloop
import costanza.george.utility.loop

/** serialize to a nicely formatted string, mimicking a kotlin dsl */
class Serializer {

    /** serialize an entity */
    fun serialize(entity: IObject) = serializeEntity(entity.reflectInfo().objectType, entity, 0)

    private fun serializeEntity(fnName: String, entity: IObject, indentLevel: Int): String {
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
        val entities = entity.reflectInfo().objects.filter { it.get() != null }
        val entityLists = entity.reflectInfo().objectLists.filter { it.list.isNotEmpty() }
        if (prims.isNotEmpty() || entities.isNotEmpty() || entityLists.isNotEmpty()) {
            // handle properties
            bld += " {\n"
            prims.forEach {
                indent(); bld += "${it.name} = ${it.get()}\n"
            }
            entities.filter { it.get() != null }.forEach {
                indent()
                bld += it.name + ":"
                val elem = it.get()!!
                bld += serializeEntity(elem.reflectInfo().objectType, elem, indent)
            }
            entityLists.forEach {
                it.list.size.iloop { index ->
                    indent()
                    val elem = it.list[index]
                    if (it.name != null) {
                        bld += it.name + ":"
                    }
                    bld += serializeEntity(elem.reflectInfo().objectType, elem, indent)
                }
            }
            indent--
            indent(); bld += "}"
        }
        bld += "\n"
        return bld.toString()
    }
}