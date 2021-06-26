//package constanza.george.reflect
//
//import costanza.george.reflect.ObjectTypeRegistry
//import costanza.george.reflect.undoredo.Changer
//import costanza.george.reflect.undoredo.ObjectChange
//import costanza.george.reflect.undoredo.ObjectPropertyChange
//import kotlin.test.Test
//import kotlin.test.assertEquals
//import kotlin.test.assertNull
//
//class UndoRedoTests {
//    @Test
//    fun testPropertyChange() {
//        val note = makeNote()
//
//        val registry = ObjectTypeRegistry()
//        registry.addAll(entityTypes)
//        val changer = Changer(note, registry)
//
//        // try undo redo
//        changer.recordChange(ObjectPropertyChange(note.inside!!, "age", "25", "23"))
//        changer.markTransaction()
//        assertEquals(25, note.inside!!.age)
//        changer.undo()
//        assertEquals(10, note.inside!!.age)
//        changer.redo()
//        assertEquals(25, note.inside!!.age)
//        changer.recordChange(ObjectPropertyChange(note.another!!, "height", "25.2", "23"))
//        changer.markTransaction()
//        assertEquals(25.2, note.another!!.height)
//        val pos = changer.pos
//
//        // now rewind, add a new change and see if the pos stays same
//        changer.undo()
//        assertEquals(20.7, note.another!!.height)
//        changer.recordChange(ObjectPropertyChange(note.another!!, "height", "30", "23"))
//        changer.markTransaction()
//        assertEquals(30.0, note.another!!.height)
//        assertEquals(pos, changer.pos)
//
//    }
//
//    @Test
//    fun testEntityChange() {
//        val note = makeNote()
//
//        val registry = ObjectTypeRegistry()
//        registry.addAll(entityTypes)
//        val changer = Changer(note, registry)
//
//        // try undo redo
//        changer.recordChange(ObjectChange(note, "inside", null))
//        changer.markTransaction()
//        assertNull(note.inside)
//
//        changer.undo()
//        assertEquals(10, note.inside!!.age)
//
//        changer.redo()
//        assertNull(note.inside)
//
//        val inside = Inside()
//        inside.age = 12
//        inside.height = 13.0
//        changer.recordChange(ObjectChange(note, "inside", inside))
//        changer.markTransaction()
//
//        assertEquals(12, note.inside!!.age)
//    }
//
//}