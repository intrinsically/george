package costanza.george.reflect.undoredo

import costanza.george.diagrams.base.Container
import costanza.george.diagrams.base.Diagram
import costanza.george.reflect.IObject
import costanza.george.utility._Map
import costanza.george.utility._map
import kotlin.random.Random

class IdAssigner {
    var id = 0
    val machine = makeRandomId()

    /** assigns missing ids and also forms a map of entities for comparison */
    fun assignAndMap(diagram: Diagram): Map<String, SavedChild> {
        diagram.id = "top" // needs an id
        val map = _map<String, SavedChild>()
        assign(diagram, map)
        map[diagram.id!!] = SavedChild("", null, 0, diagram)
        return map
    }

    /** recursively descend down */
    private fun assign(top: IObject, map: _Map<String, SavedChild>) {
        top.reflectInfo().objectLists.forEach {
            val listProp = it
            listProp.list.forEachIndexed { index, obj ->
                val id = obj.reflectInfo().id ?: makeObjectId()
                obj.reflectInfo().id = id
                map[id] = SavedChild(top.reflectInfo().id!!, listProp.name, index, obj)
                assign(obj, map)
            }
        }
    }

    /** make a unique Id */
    fun makeObjectId() = "$machine${makeRandomId()}-${id++ % 100}"
    /** create a random id between 0 and RANGE - 1 */
    fun makeRandomId() = "${makeRandomChar()}${makeRandomChar()}"
    fun makeRandomChar() = (Random.nextInt(26) + 65).toChar()
}