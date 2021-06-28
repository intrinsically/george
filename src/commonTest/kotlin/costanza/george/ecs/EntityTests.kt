package costanza.george.ecs

import costanza.george.components.CBounds
import costanza.george.components.CBoundsCircle
import costanza.george.components.CBoundsSquare
import costanza.george.geometry.Coord
import costanza.george.geometry.Dim
import costanza.george.reflect.operations.Serializer
import costanza.george.reflect.typedproperties.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class TestEntity : Entity() {
    override fun entityType() = "TestEntity"

    val bounds = CBounds(this, Coord(8, 8), Dim(10, 10))
    val square = CBoundsSquare(this, Coord(10, 10), 23.0)
}

open class BaseEntity : Entity() {
    override fun entityType() = "BaseEntity"

    val bounds = CBounds(this, Coord(8, 8), Dim(10, 10))
    val square = CBoundsSquare(this, Coord(10, 10), 23.0, "square-")
}

class InheritingEntity : BaseEntity() {
    override fun entityType() = "InheritingEntity"

    val circle = CBoundsCircle(this, Coord(12, 12), 10.0, "circle-")
    var address: String = "here"
    val prop_address = StringProperty(this, "address", false, "", { address }, { address = it })
}

class ECSTests {
    @Test
    fun testEntity() {
        val entity = TestEntity()

        val bounds: CBounds? = entity.component()
        val square: CBoundsSquare? = entity.component()
        val circle: CBoundsCircle? = entity.component()

        // have we got the right components
        assertEquals(Dim(10, 10), bounds!!.dim)
        assertEquals(23.0, square!!.side)
        assertNull(circle)

        println("Found bounds component ${bounds.dim}")
        println("Found square component ${square.side}")
        println("Found null circle component $circle")

        printProps(entity)

        // we should have a single dupliace
        val dups = entity.findDuplicates()
        assertEquals("loc", dups.joinToString(","))
        println("Duplicates = $dups")
    }

    @Test
    fun testEntitySerialization() {
        val serial = Serializer()
        val entity = InheritingEntity()
        println(serial.serialize(entity))
        printProps(entity)
        // number of props is 7
        assertEquals(8, entity.properties.size)
    }

    private fun printProps(entity: Entity) {
        // get the properties
        entity.reflectInfo().properties.forEach {
            println("Property ${it.name}")
        }
        println("----")
    }
}