package costanza.george.properties

import costanza.george.reflect.*
import costanza.george.reflect.operations.Deserializer
import costanza.george.reflect.operations.Serializer
import costanza.george.reflect.typedproperties.StringProperty
import costanza.george.reflect.typedproperties.extraSlash
import costanza.george.reflect.typedproperties.removeExtraSlash
import kotlin.test.Test
import kotlin.test.assertEquals

class StringTokenTests {
    @Test
    fun testStringWrapAndUnwrap() {
        val str = """my \name "Andrew" means "something""""

        val extra = extraSlash(str)
        println("adding 1 slash $extra")
        assertEquals("""my \\name \"Andrew\" means \"something\"""", extra)

        val extraextra = extraSlash(extra)
        println("adding 2 slash $extraextra")
        assertEquals("""my \\\name \\"Andrew\\" means \\"something\\"""", extraextra)

        val uextraextra = removeExtraSlash(extraextra)
        println("unwrapping 2 slash $uextraextra")
        assertEquals("""my \\name \"Andrew\" means \"something\"""", uextraextra)

        val uextra = removeExtraSlash(extra)
        println("unwrapping 1 slash $uextra")
        assertEquals(str, uextra)
    }

    class TestEntity(var name: String = "", var address: String = ""): IReflect, ReflectBase() {
        override val objectType = "testentity"
        var prop_name = StringProperty(this, "name", false, "", {name}, {name = it})
        var prop_address = StringProperty(this, "address", false, "", {address}, {address = it})
    }

    @Test
    fun testPopStrings() {
        val name = """ "andrew" """
        val addr = """ "ma\l\ibu! """
        val first = TestEntity(name, addr)
        val serial = Serializer().serialize(first)
        var second = TestEntity(serial, serial)
        val sserial = Serializer().serialize(second)

        println(sserial)

        // now deserialize in 2 parts
        val registry = ObjectTypeRegistry()
        registry.add(ObjectType { TestEntity() })

        // first part
        val dsecond: TestEntity = Deserializer(registry).deserialize(TokenProvider(sserial))
        assertEquals(serial, dsecond.name)
        assertEquals(serial, dsecond.address)

        // second part
        val dfirst: TestEntity = Deserializer(registry).deserialize(TokenProvider(dsecond.name))
        assertEquals(name, dfirst.name)
        assertEquals(addr, dfirst.address)
    }
}