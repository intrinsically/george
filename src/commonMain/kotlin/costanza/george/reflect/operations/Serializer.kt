package costanza.george.reflect.operations

import costanza.george.reflect.IReflect
import costanza.george.utility.iloop
import costanza.george.utility.loop

/** serialize to a nicely formatted string, mimicking a kotlin dsl */
class Serializer {

    /** serialize an entity */
    fun serialize(entity: IReflect) = serializeEntity(entity.objectType, entity, 0)

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

        // handle constructor parameters - but omit trailing nulls
        val cons = entity.properties.filter { it.isConstructor }
        var lastNonNull = -1
        cons.forEachIndexed { pos, prop ->
            if (prop.get() != "null") {
                lastNonNull = pos
            }
        }
        if (lastNonNull != -1) {
            // we have some constructor parameters
            val use = cons.subList(0, lastNonNull + 1)
            val constr = use.joinToString { it.get() }
            bld += "($constr)"
        }

        // only add the block if we have actual properties
        val prims = entity.properties.filter { !it.isConstructor && !it.isDefault() }
        val entities = entity.objects.filter { it.get() != null }
        val entityLists = entity.objectLists.filter { it.list.isNotEmpty() }
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
                bld += serializeEntity(elem.objectType, elem, indent)
            }
            entityLists.forEach {
                it.list.size.iloop { index ->
                    indent()
                    val elem = it.list[index]
                    if (it.name != null) {
                        bld += it.name + ":"
                    }
                    bld += serializeEntity(elem.objectType, elem, indent)
                }
            }
            indent--
            indent(); bld += "}"
        }
        bld += "\n"
        return bld.toString()
    }
}