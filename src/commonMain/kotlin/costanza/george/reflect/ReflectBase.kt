package costanza.george.reflect

import costanza.george.reflect.typedproperties.OptionalStringProperty
import costanza.george.reflect.typedproperties.StringProperty
import costanza.george.utility._List
import costanza.george.utility._list

abstract class ReflectBase: IReflect {
    /** id is assigned if we need to reference the object by name */
    override var id: String = ""
    override val properties: _List<PrimitiveProperty> = _list(
        StringProperty(null, "id", false, "", {id}) { id = it })
    override val objects: _List<ObjectProperty> = _list()
    override val objectLists: _List<ObjectListProperty<IReflect>> = _list()
}

