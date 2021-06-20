package costanza.george.ecs

import costanza.george.geometry.Coord
import costanza.george.geometry.Dim
import costanza.george.reflect.operations.Serializer
import costanza.george.reflect.typedproperties.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class CBounds(
    entity: Entity,
    var loc: Coord,
    var dim: Dim,
    prefix: String = ""
) : Component(entity) {
    val prop_loc =
        CoordProperty(entity, prefix + "loc", false, Coord(0, 0), { loc }, { loc = it })
    val prop_dim =
        DimProperty(entity, prefix + "dim", false, Dim(0, 0), { dim }, { dim = it })
}

class CBoundsCircle(
    entity: Entity,
    var loc: Coord,
    var radius: Double,
    prefix: String = ""
) : Component(entity) {
    val prop_loc =
        CoordProperty(entity, prefix + "loc", false, Coord(0, 0), { loc }, { loc = it })
    val prop_radius =
        DoubleProperty(entity, prefix + "radius", false, 0.0, { radius }, { radius = it })
}

class CBoundsSquare(entity: Entity, var loc: Coord, var side: Double, prefix: String = "") : Component(entity) {
    val prop_loc =
        CoordProperty(entity, prefix + "loc", false, Coord(14, 14), { loc }, { loc = it })
    val prop_side =
        DoubleProperty(entity, prefix + "side", false, 10.0, { side }, { side = it })
}

class TestEntity(entityName: String = "testentity") : Entity(entityName) {
    val bounds = CBounds(this, Coord(8, 8), Dim(10, 10))
    val square = CBoundsSquare(this, Coord(10, 10), 23.0)
}

open class BaseEntity(entityName: String = "testentity") : Entity(entityName) {
    val bounds = CBounds(this, Coord(8, 8), Dim(10, 10))
    val square = CBoundsSquare(this, Coord(10, 10), 23.0, "square-")
}

class InheritingEntity(entityName: String = "inheritingentity") : BaseEntity(entityName) {
    val circle = CBoundsCircle(this, Coord(12, 12), 10.0, "circle-")
    var address: String = "here"
    val prop_address = string("address", false, "", { address }, { address = it })
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
        // number of pros is 7
        assertEquals(7, entity.properties.size)
    }

    private fun printProps(entity: Entity) {
        // get the properties
        entity.reflectInfo().properties.forEach {
            println("Property ${it.name}")
        }
        println("----")
    }
}