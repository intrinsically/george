package costanza.george.undoredo

import costanza.george.diagrams.base.Diagram
import costanza.george.diagrams.classpalette.Klass
import costanza.george.diagrams.drawingEntityTypes
import costanza.george.diagrams.infopalette.Area
import costanza.george.reflect.ObjectTypeRegistry
import costanza.george.reflect.operations.Serializer
import costanza.george.reflect.undoredo.Changer
import costanza.george.reflect.undoredo.IdAssigner
import costanza.george.reflect.undoredo.Differ
import kotlin.test.Test
import kotlin.test.assertEquals

class UndoTests {
    @Test
    fun testUndoProperty() {

        // register the object types for drawing
        val reg = ObjectTypeRegistry()
        reg.addAll(drawingEntityTypes)

        // make a simple diagram
        val diag = Diagram()
        diag.name = "Test"
        val sh1 = Klass()
        sh1.name = "klass1"
        val sh2 = Klass()
        sh2.name = "klass2"
        diag.shapes += sh1
        diag.shapes += sh2

        // record the changes so we can diff later
        val ids = IdAssigner()

        // find differences
        val diff = Differ(ids, reg, diag)
        val changes = diff.determineChanges()

        // nothing changed yet
        assertEquals(0, changes.size) // nothing changed yet

        // now make a change
        sh1.name = "class1"
        diag.name = "Updated"
        val changesA = diff.determineChanges()
        assertEquals(2, changesA.size)

        // now roll back and see
        val changer = Changer(diag, reg)
        changer.recordChanges(changesA)
        changer.markTransaction()
        changer.undo()

        assertEquals("klass1", sh1.name)
        assertEquals("Test", diag.name)

        changer.redo()

        assertEquals("class1", sh1.name)
        assertEquals("Updated", diag.name)

        println(Serializer().serialize(diag))
        println(changesA)
    }

    @Test
    fun testUndoAddAndDelete() {

        // register the object types for drawing
        val reg = ObjectTypeRegistry()
        reg.addAll(drawingEntityTypes)

        // make a simple diagram
        val diag = Diagram(); diag.name = "Test"

        val sh1 = Klass(); sh1.name = "klass1"
        val sh2 = Klass(); sh2.name = "klass2"
        val sh3 = Klass(); sh3.name = "klass3"
        diag.shapes += listOf(sh1, sh2, sh3)

        // add an area so we can move also
        val ar1 = Area(); ar1.name = "area1"
        diag.shapes += ar1
        val sh4 = Klass(); sh4.name = "klass4"
        val sh5 = Klass(); sh5.name = "klass5"
        ar1.shapes += listOf(sh4, sh5)
        val ids = IdAssigner()
        ids.assignAndMap(diag)
        var old = Serializer().serialize(diag)

        // mark the state of the world before the change
        val diff = Differ(ids, reg, diag)

        // perform a delete
        diag.shapes.removeAt(1)
        val sh6 = Klass(); sh6.name = "klass6"
        ar1.shapes.add(sh6)

        // test it out to see if we can undo & redo correctly
        testIt(old, diff, diag, 2)
    }

    @Test
    fun testMoveBetween() {

        // register the object types for drawing
        val reg = ObjectTypeRegistry()
        reg.addAll(drawingEntityTypes)

        // make a simple diagram
        val diag = Diagram(); diag.name = "Test"

        val sh1 = Klass(); sh1.name = "klass1"
        val sh2 = Klass(); sh2.name = "klass2"
        val sh3 = Klass(); sh3.name = "klass3"
        diag.shapes += listOf(sh1, sh2, sh3)

        // add an area so we can move also
        val ar1 = Area(); ar1.name = "area1"
        diag.shapes += ar1
        val sh4 = Klass(); sh4.name = "klass4"
        val sh5 = Klass(); sh5.name = "klass5"
        ar1.shapes += listOf(sh4, sh5)
        val ids = IdAssigner()
        ids.assignAndMap(diag)
        var old = Serializer().serialize(diag)

        // mark the state of the world before the change
        val diff = Differ(ids, reg, diag)

        // perform an inter list move
        val x = diag.shapes.removeAt(1)
        ar1.shapes.add(x)

        // test it out to see if we can undo & redo correctly
        testIt(old, diff, diag, 1)
    }

    /** check that undo and redo produce the correct diagrams by looking at the serialized state */
    private fun testIt(old: String, diff: Differ, diag: Diagram, numChanges: Int) {
        val registry = ObjectTypeRegistry()
        registry.addAll(drawingEntityTypes)
        val changer = Changer(diag, registry)
        val changes = diff.determineChanges()
        changer.recordChanges(changes)
        println("Changes = $changes")
        changer.markTransaction()

        val next = Serializer().serialize(diag)
        changer.undo()
        assertEquals(old, Serializer().serialize(diag))
        changer.redo()
        assertEquals(next, Serializer().serialize(diag))
        assertEquals(numChanges, changes.size)
    }
}