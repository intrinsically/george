package costanza.george.properties

import costanza.george.reflect.typedproperties.extraSlash
import costanza.george.reflect.typedproperties.removeExtraSlash
import kotlin.test.Test
import kotlin.test.assertEquals

class StringTokenTests {
    @Test
    fun testStringWrapAndUnwrap() {
        val str = "my name \"Andrew\" means \"something\""

        val extra = extraSlash(str)
        println("adding 1 slash $extra")
        assertEquals("my name \\\"Andrew\\\" means \\\"something\\\"", extra)

        val extraextra = extraSlash(extra)
        println("adding 2 slash $extraextra")
        assertEquals("my name \\\\\"Andrew\\\\\" means \\\\\"something\\\\\"", extra)

        val uextraextra = removeExtraSlash(extraextra)
        println("unwrapping 2 slash $uextraextra")
        assertEquals("my name \\\"Andrew\\\" means \\\"something\\\"", extra)

        val uextra = removeExtraSlash(extra)
        println("unwrapping 1 slash $uextra")
        assertEquals("my name \\\"Andrew\\\" means \\\"something\\\"", uextra)
    }
}