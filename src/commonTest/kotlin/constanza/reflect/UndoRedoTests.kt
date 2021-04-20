package constanza.reflect

import costanza.reflect.EntityTypeRegistry
import costanza.reflect.operations.Changer
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class UndoRedoTests {

    @Test
    fun testPropertyChange() {
        val note = makeNote()
        note.inside!!.id = "a"
        note.another!!.id = "b"

        val registry = EntityTypeRegistry()
        registry.addAll(entityTypes)
        val changer = Changer(note, registry)

        // try undo redo
        changer.changeProperty("a", "age", "25")
        changer.mark()
        assertEquals(25, note.inside!!.age)
        changer.back()
        assertEquals(10, note.inside!!.age)
        changer.fwd()
        assertEquals(25, note.inside!!.age)
        changer.changeProperty("b", "height", "25.2")
        changer.mark()
        assertEquals(25.2, note.another!!.height)
        var pos = changer.pos

        // now rewind, add a new change and see if the pos stays same
        changer.back()
        assertEquals(20.7, note.another!!.height)
        changer.changeProperty("b", "height", "30")
        changer.mark()
        assertEquals(30.0, note.another!!.height)
        assertEquals(pos, changer.pos)

    }

    @Test
    fun testEntityChange() {
        val note = makeNote()
        note.inside!!.id = "a"
        note.another!!.id = "b"

        val registry = EntityTypeRegistry()
        registry.addAll(entityTypes)
        val changer = Changer(note, registry)

        // try undo redo
        changer.setEntity("x", "inside", null)
        changer.mark()
        assertNull(note.inside)

        changer.back()
        assertEquals(10, note.inside!!.age)

        changer.fwd()
        assertNull(note.inside)

        var inside = Inside()
        inside.age = 12
        inside.height = 13.0
        changer.setEntity("x", "inside", inside)
        changer.mark()

        assertEquals(12, note.inside!!.age)
    }

}