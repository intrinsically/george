package costanza.george.ecs

import costanza.george.reflect.IObject
import costanza.george.reflect.ReflectInfo
import costanza.george.utility._map

abstract class Entity: ReflectInfo(""), IObject {
    val components = _map<String, Component>()
    val behaviors = _map<String, IBehavior>()
    fun add(component: Component) = components.put(component::class.simpleName!!, component)
    fun add(behavior: IBehavior) = behaviors.put(behavior::class.simpleName!!, behavior)
    override fun reflectInfo() = this
    /** override with entity type */
    abstract fun entityType(): String

    init {
        objectType = entityType()
    }

    /** get a component by the class of the component */
    inline fun <reified C: Component> component() = components[C::class.simpleName!!] as C?

    /** get any duplicate names across different components - duplicate names messes with serialization */
    fun findDuplicates(): List<String> {
        val seen = mutableSetOf<String>()
        val dups = mutableListOf<String>()
        properties.forEach { it ->
            if (seen.contains(it.name)) {
                dups += it.name
            }
            seen += it.name
        }
        return dups
    }
}
