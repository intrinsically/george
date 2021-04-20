package constanza.reflect

import costanza.geometry.Rect
import costanza.reflect.*
import costanza.reflect.operations.Deserializer
import costanza.reflect.operations.Serializer
import costanza.reflect.typedproperties.*
import costanza.utility.list
import kotlin.test.Test
import kotlin.test.assertEquals


var entityTypes = list(
    EntityType("note") { Note() },
    EntityType("inside") { Inside() },
)


class Note: ITopEntity {
    private var count = 0
    val prefix = makePrefix()
    override var id: String = "x"
    override fun find(id: String): IEntity? {
        if (id == this.id) { return this }
        if (inside != null && inside!!.id == id) { return inside }
        if (another != null && another!!.id == id) { return another }
        return null
    }

    override fun makeId() = prefix + count++

    //region >reflection details
    override val entityType: String = "note"
    override val properties = list(
        IntProperty("count", false, 0, {count}, {count = it}),
        StringProperty("name", true, "", {name}, {name = it}),
        StringProperty("details", false, "", {details}, {details = it}),
        RectProperty("location", false, Rect(0,0,0,0), {location}, {location = it}),
    )
    override val entities = list(
        EntityProperty("inside", "inside", { inside }, { inside = it as Inside? }),
        EntityProperty("another", "inside", { another }, { another = it as Inside? })
    )
    //endregion

    var name = ""
    var details = ""
    var location = Rect(0,0,0,0)

    var inside: Inside? = null
    var another: Inside? = null
}

class Inside: IEntity {
    override var id: String = ""
    override val entityType = "inside"
    override val properties = listOf(
        StringProperty("id", false, "", {id}, {id = it}),
        IntProperty("age", true, 0, {age}, {age = it}),
        DoubleProperty("height", false, 0.0, {height}, {height = it})
    )
    override val entities = list<IEntityProperty>()

    var age = 0
    var height = 0.0
}

fun makeNote(): Note {
    val note = Note()
    note.details = "This is a note"
    note.name = "Andrew's Note"
    note.inside = Inside()
    note.location = Rect(10,35,20,20)
    note.inside!!.age = 10
    note.another = Inside()
    note.another!!.age = 20
    note.another!!.height = 20.7
    return note
}

class SerializationTests {
    @Test
    fun testNote() {
        val note = makeNote()
        val registry = EntityTypeRegistry()
        registry.addAll(entityTypes)
        val serial = Serializer()
        val str = serial.serialize(note)
        println(str)

        // now deserialize
        val deserial = Deserializer(registry)
        val prov = TokenProvider(str)
        val dnote = deserial.deserialize(Note(), prov)
        val dstr = serial.serialize(dnote)
        println(dstr)

        // serialize again and deserialize - should be same string
        val nprov = TokenProvider(dstr)
        val nnote = Note()
        val ndnote = deserial.deserialize(nnote, nprov)
        val ndstr = serial.serialize(ndnote)
        println(ndstr)

        // should be equal
        assertEquals(dstr, ndstr)
        println("Prefix = ${nnote.prefix}")
    }
}