package costanza.george.reflect

import costanza.george.utility._List

class ObjectListProperty<T: IObject>(
    ri: ReflectInfo,
    val name: String?,
    val list: _List<T>
) {
    init {
        ri.objectLists += this as ObjectListProperty<IObject>
    }
}

