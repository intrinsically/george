package constanza.reflect

import costanza.reflect.EntityType
import costanza.reflect.IEntity
import costanza.reflect.typedproperties.StringProperty
import kotlin.test.Test



class Note: IEntity {
    companion object {
        var entityTypes = listOf(
            EntityType("note") { Note() }
        )
    }
    override val properties = listOf(
        StringProperty("name", {name}, {name = it}),
        StringProperty("details", {details}, {details = it}),
    )

    var name: String = ""
    var details: String = ""

}

class ReflectTests {
    @Test
    fun testNote() {
    }
}