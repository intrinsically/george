package constanza.reflect

import costanza.reflect.EntityType
import costanza.reflect.EntityTypeRegistry
import costanza.reflect.IEntity
import costanza.reflect.Serializer
import costanza.reflect.typedproperties.DoubleProperty
import costanza.reflect.typedproperties.EntityProperty
import costanza.reflect.typedproperties.IntProperty
import costanza.reflect.typedproperties.StringProperty
import utility.list
import kotlin.test.Test



var entityTypes = list(
    EntityType("note") { Note() },
    EntityType("inside") { Inside() },
)


class Note: IEntity {
    override val entityName = "note"
    override val properties = listOf(
        StringProperty("name", true, "", {name}, {name = it}),
        StringProperty("details", false, "", {details}, {details = it}),
        EntityProperty("inside", "Inside", { inside }, {})
    )

    var name: String = ""
    var details: String = ""

    var inside: Inside? = null
}

class Inside: IEntity {
    override val entityName = "inside"
    override val properties = listOf(
        IntProperty("age", true, 0, {age}, {age = it}),
        DoubleProperty("height", true, 0.0, {height}, {height = it})
    )
    var age = 0
    var height = 0.0

}

class ReflectTests {
    @Test
    fun testNote() {

        val note = Note()
        note.details = "This is a note"
        note.name = "Andrew's Note"
        note.inside = Inside()
        note.inside!!.age = 10

        val registry = EntityTypeRegistry()
        registry.addAll(entityTypes)
        val serial = Serializer(registry)
        println(serial.serialize(note.entityName, note))
    }
}