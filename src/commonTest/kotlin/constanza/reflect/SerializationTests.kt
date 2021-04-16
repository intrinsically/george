package constanza.reflect

import costanza.geometry.Rect
import costanza.reflect.*
import costanza.reflect.typedproperties.*
import utility.list
import kotlin.test.Test
import kotlin.test.assertEquals


var entityTypes = list(
    EntityType("Note") { Note() },
    EntityType("Inside") { Inside() },
)


class Note: IEntity {
    override val entityName = "Note"
    override val properties = listOf(
        StringProperty("name", true, "", {name}, {name = it}),
        StringProperty("details", false, "", {details}, {details = it}),
        RectProperty("location", false, Rect(0,0,0,0), {location}, {location = it}),
        EntityProperty("inside", "Inside", { inside }, { inside = it as Inside }),
        EntityProperty("another", "Inside", { another }, { another = it as Inside })
    )

    var name = ""
    var details = ""
    var location = Rect(0,0,0,0)

    var inside: Inside? = null
    var another: Inside? = null
}

class Inside: IEntity {
    override val entityName = "Inside"
    override val properties = listOf(
        IntProperty("age", true, 0, {age}, {age = it}),
        DoubleProperty("height", false, 0.0, {height}, {height = it})
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
        note.location = Rect(10,35,20,20)
        note.inside!!.age = 10
        note.another = Inside()
        note.another!!.age = 20
        note.another!!.height = 20.7

        val registry = EntityTypeRegistry()
        registry.addAll(entityTypes)
        val serial = Serializer(registry)
        val str = serial.serialize(note)
        println(str)

        // now deserialize
        val deserial = Deserializer(registry)
        val prov = TokenProvider(str)
        val dnote = deserial.deserialize("Note", prov)
        val dstr = serial.serialize(dnote)
        println(dstr)

        // should be equal
        assertEquals(str, dstr)
    }
}