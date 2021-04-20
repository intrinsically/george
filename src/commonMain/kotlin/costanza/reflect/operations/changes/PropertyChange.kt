package costanza.reflect.operations.changes

import costanza.reflect.ITopEntity
import costanza.reflect.TokenProvider
import costanza.reflect.operations.findPrimitiveProperty

class PropertyChange(
    val top: ITopEntity,
    val entityId: String,
    val propName: String,
    val value: String): IChange {

    init { fwd() }

    var old: String? = null

    override fun back() {
        doit(old!!)
    }

    override fun fwd() {
        old = doit(value)
    }

    private fun doit(nextVal: String): String {
        val ent = top.find(entityId) ?: throw Exception("Cannot find entity $entityId")
        val prop = findPrimitiveProperty(ent, propName) ?: throw Exception("Cannot find property $propName")
        val old = prop.get()
        prop.set(TokenProvider(nextVal))
        return old
    }
}