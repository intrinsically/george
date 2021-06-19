package costanza.george.ecs

import costanza.george.utility._list
import costanza.george.utility._map

abstract class Entity {
    var components = _map<String, Component>()
    var behaviors = _map<String, IBehavior>()
    fun add(component: Component) = components.put(component::class.simpleName!!, component)
    fun add(behavior: IBehavior) = behaviors.put(behavior::class.simpleName!!, behavior)
    inline fun <reified C: Component> get() = components[C::class.simpleName!!] as C?

    /** get all the reflection properties as a list */
    fun properties() = components.values.flatMap { it.properties }
    fun entities() = components.values.flatMap { it.entities }
    fun entityLists() = components.values.flatMap { it.entityLists }

    /** get any duplicate names across different components - messes with serialization */
    fun findDuplicates(): List<String> {
        val seen = _map<String, String>()
        val dups = _list<String>()
        components.forEach {
            val cName = it.key
            it.value.properties.forEach {
                if (seen.contains(it.name)) {
                    dups.add(seen[it.name]!!)
                    dups.add("$cName::${it.name}")
                }
                seen[it.name] = "$cName::${it.name}"
            }
        }
        return dups
    }
}
