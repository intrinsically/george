package constanza.reflect

import costanza.george.reflect.EntityTypeRegistry
import costanza.george.reflect.operations.Changer
import costanza.george.reflect.changes.EntityChange
import costanza.george.reflect.changes.PropertyChange
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class UndoRedoTests {

    @Test
    fun testPropertyChange() {
        val note = makeNote()

        val registry = EntityTypeRegistry()
        registry.addAll(entityTypes)
        val changer = Changer(note, registry)

        // try undo redo
        changer.makeChange(PropertyChange(note.inside!!, "age", "25"))
        changer.mark()
        assertEquals(25, note.inside!!.age)
        changer.back()
        assertEquals(10, note.inside!!.age)
        changer.fwd()
        assertEquals(25, note.inside!!.age)
        changer.makeChange(PropertyChange(note.another!!, "height", "25.2"))
        changer.mark()
        assertEquals(25.2, note.another!!.height)
        val pos = changer.pos

        // now rewind, add a new change and see if the pos stays same
        changer.back()
        assertEquals(20.7, note.another!!.height)
        changer.makeChange(PropertyChange(note.another!!, "height", "30"))
        changer.mark()
        assertEquals(30.0, note.another!!.height)
        assertEquals(pos, changer.pos)

    }

    @Test
    fun testEntityChange() {
        val note = makeNote()

        val registry = EntityTypeRegistry()
        registry.addAll(entityTypes)
        val changer = Changer(note, registry)

        // try undo redo
        changer.makeChange(EntityChange(note, "inside", null))
        changer.mark()
        assertNull(note.inside)

        changer.back()
        assertEquals(10, note.inside!!.age)

        changer.fwd()
        assertNull(note.inside)

        val inside = Inside()
        inside.age = 12
        inside.height = 13.0
        changer.makeChange(EntityChange(note, "inside", inside))
        changer.mark()

        assertEquals(12, note.inside!!.age)
    }

}