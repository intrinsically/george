package costanza.george.undoredo

import costanza.george.diagrams.base.Diagram
import costanza.george.diagrams.classpalette.Klass
import costanza.george.diagrams.drawingEntityTypes
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
        ids.assignAndMap(diag)

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
}