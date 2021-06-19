package costanza.george.ecs

import costanza.george.geometry.Coord
import costanza.george.geometry.Dim
import costanza.george.reflect.typedproperties.coord
import costanza.george.reflect.typedproperties.dim
import costanza.george.reflect.typedproperties.double
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class CBounds(entity: Entity, var loc: Coord, var dim: Dim, prefix: String = ""): Component(entity) {
    init {
        coord(prefix + "loc", false, Coord(0,0), { loc }, { loc = it })
        dim(prefix + "dim", false, Dim(0,0), { dim }, { dim = it })
    }
}

class CBoundsCircle(entity: Entity, var loc: Coord, var radius: Double, prefix: String = ""): Component(entity) {
    init{
        coord(prefix + "loc", false, Coord(0,0), { loc }, { loc = it })
        double(prefix + "radius", false, 10.0, { radius }, { radius = it })
    }
}

class CBoundsSquare(entity: Entity, var loc: Coord, var side: Double, prefix: String = ""): Component(entity) {
    init {
        coord(prefix + "loc", false, Coord(0,0), { loc }, { loc = it })
        double(prefix + "side", false, 10.0, { side }, { side = it })
    }
}

class TestEntity: Entity() {
    val bounds = CBounds(this, Coord(0,0), Dim(10,10))
    val square = CBoundsSquare(this, Coord(0,0), 23.0)
}

class TestEntity2: Entity() {
    val bounds = CBounds(this, Coord(0,0), Dim(10,10))
    val square = CBoundsSquare(this, Coord(0,0), 23.0, "x")
}

class ECSTests {
    @Test
    fun testEntity() {
        val entity = TestEntity()
        testIt(entity)

        // we should have some dups
        val dups = entity.findDuplicates()
        assertEquals("CBounds::loc,CBoundsSquare::loc", dups.joinToString(","))
        println("Duplicates = $dups")
    }

    @Test
    fun testEntity2() {
        val entity = TestEntity2()
        testIt(entity)

        // we should have no dups
        val dups = entity.findDuplicates()
        assertEquals(0, dups.size)
        println("No duplicates!")
    }

    fun testIt(entity: Entity) {
        val bounds:CBounds? = entity.get()
        val square:CBoundsSquare? = entity.get()
        val circle:CBoundsCircle? = entity.get()

        // have we got the right components
        assertEquals(Dim(10,10), bounds!!.dim)
        assertEquals(23.0, square!!.side)
        assertNull(circle)

        println("Found bounds component ${bounds!!.dim}")
        println("Found square component ${square!!.side}")
        println("Found null circle component $circle")

        // get the properties
        entity.properties().forEach {
            println("Property ${it.name}")
        }
        println("----")
    }
}