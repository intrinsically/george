package constanza.george.reflect

import costanza.george.diagrams.Together
import costanza.george.diagrams.base.FontDetails
import costanza.george.diagrams.base.ITextCalculator
import costanza.george.reflect.ObjectTypeRegistry
import costanza.george.reflect.TokenProvider
import costanza.george.reflect.operations.Deserializer
import costanza.george.reflect.operations.Serializer
import kotlin.test.Test
import kotlin.test.assertEquals


class SerializationTests {
    @Test
    fun testNote() {
        val note = makeNote()
        val registry = ObjectTypeRegistry()
        registry.addAll(entityTypes)
        val serial = Serializer()
        val str = serial.serialize(note)
        println(str)

        // now deserialize
        val deserial = Deserializer(registry)
        val prov = TokenProvider(str)
        val dnote: Note = deserial.deserialize(prov)
        val dstr = serial.serialize(dnote)
        println(dstr)

        // serialize again and deserialize - should be same string
        val nprov = TokenProvider(dstr)
        val ndnote: Note = deserial.deserialize(nprov)
        val ndstr = serial.serialize(ndnote)
        println(ndstr)

        // should be equal
        assertEquals(dstr, ndstr)
    }

    @Test
    fun testDiagram() {
        val calc = object: ITextCalculator {
            override fun calcHeight(details: FontDetails): Double = 20.0
            override fun calcWidth(details: FontDetails, minWidth: Double, text: String): Double = 10.0
        }

        val tog = Together()
        val diag = tog.makeDiagram(calc)
        val str = tog.serialize(diag)
        println(str)

        // now try to deserialize
        val ndiag = tog.makeDiag(calc, str)
        val nstr = tog.serialize(ndiag)
        println(nstr)
        assertEquals(str, nstr)
    }
}