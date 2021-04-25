package constanza.reflect

import costanza.geometry.Rect
import costanza.reflect.*
import costanza.reflect.operations.Deserializer
import costanza.reflect.operations.Serializer
import costanza.reflect.typedproperties.*
import costanza.utility.list
import kotlin.test.Test
import kotlin.test.assertEquals



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
    }
}