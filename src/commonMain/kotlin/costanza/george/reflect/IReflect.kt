package costanza.george.reflect

import costanza.george.reflect.typedproperties.OptionalStringProperty
import costanza.george.utility._List
import costanza.george.utility._list

/** an object implementing this can provide reflection information about itself */
interface IReflect {
    val objectType: String
    /** id is assigned if we need to reference the object by name */
    var id: String
    val properties: _List<PrimitiveProperty>
    val objects: _List<ObjectProperty>
    val objectLists: _List<ObjectListProperty<IReflect>>

    fun findPrimitiveProperty(name: String) =
        properties.find { it.name == name }

    fun findObjectProperty(fnName: String) =
        objects.find { it.name == fnName }

    fun findListProperty(fnName: String?) =
        objectLists.find { it.name == fnName }
}