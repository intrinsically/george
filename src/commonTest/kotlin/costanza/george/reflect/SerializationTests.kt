package constanza.george.reflect

import costanza.george.diagrams.Together
import costanza.george.diagrams.base.FontDetails
import costanza.george.diagrams.base.ITextCalculator
import costanza.george.reflect.EntityTypeRegistry
import costanza.george.reflect.TokenProvider
import costanza.george.reflect.operations.Deserializer
import costanza.george.reflect.operations.Serializer
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

    @Test
    fun testDiagram() {
        var calc = object: ITextCalculator {
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