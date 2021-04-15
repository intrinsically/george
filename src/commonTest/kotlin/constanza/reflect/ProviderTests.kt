package constanza.reflect

import costanza.reflect.TokenProvider
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class ProviderTests {
    @Test
    fun testParsing() {
        val s = "andrew 10 20 1.5 1.3e-10 X \"hello there\""
        val prov = TokenProvider(s)
        assertEquals("andrew", prov.popName())
        assertEquals(10, prov.popInt())
        assertEquals(20.0, prov.popDouble())
        assertEquals(1.5, prov.popDouble())
        assertEquals(1.3e-10, prov.popDouble())
        prov.popChar('X')
        assertEquals("hello there", prov.popString())
    }

    @Test
    fun testBadInt() {
        val s = """andrew
            foo
        """
        val prov = TokenProvider(s)
        assertEquals("andrew", prov.popName())
        val except = assertFailsWith<Exception> {
            prov.popInt()
        }
        assertEquals("Looking for integer at (1, 13)", except.message)
    }

    @Test
    fun testPeek() {
        val s = "abc"
        val prov = TokenProvider(s)
        assertEquals('a', prov.peek())
        assertEquals('a', prov.pop())
        assertEquals('b', prov.peek())
        assertEquals('b', prov.pop())
        assertEquals('c', prov.pop())
        assertNull(prov.pop())
    }
}