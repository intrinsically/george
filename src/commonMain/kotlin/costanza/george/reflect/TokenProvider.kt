package costanza.george.reflect

class TokenProvider(var s: String): IProvider {
    private var row = 0
    private var col = 0
    private var pos = 0
    private val size = s.length
    private var peeked: Char? = null

    private fun popToken(lookingFor: String,  skip: Boolean, pred: (Char, Char) -> Boolean): String {
        if (skip) {
            skip()
        }
        var name = ""
        var prev = ' '
        do {
            val c = peek()
            if (c != null && pred(prev, c)) {
                name += pop()
            } else {
                return name.ifEmpty {
                    throw Exception("Looking for $lookingFor at ($row, $col)")
                }
            }
            prev = c
        } while (true)
    }

    /** read various token types */
    override fun popName() = popToken("entityType", true) { _, c -> c.isLetterOrDigit() || c == '_' }
    override fun popName(name: String): String {
        val pop = popName()
        if (pop != name) {
            throw Exception("Looking for $name at ($row, $col)")
        }
        return pop
    }
    override fun popInt() = popToken("integer", true) { _, c -> c.isDigit() }.toInt()
    override fun popDouble() = popToken("double", true) { p, c -> c.isDigit() || "+-.eE".contains(c) }.toDouble()
    override fun popChar(char: Char): Char {
        skip()
        if (pop() != char) {
            throw Exception("Looking for $char at ($row, $col)")
        }
        return char
    }
    override fun popString(): String {
        popChar('"')
        val str = popToken("string", false) { p, c -> p == '\\' || c != '\"' }
        popChar('"')
        return str
    }

    /** skip any whitespace */
    override fun skip() {
        while (peek() != null && peek()!!.isWhitespace()) {
            pop()
        }
    }

    override fun pop(): Char? {
        if (peeked != null) {
            val ret = peeked!!
            peeked = null
            return ret
        }
        if (pos == size) {
            return null
        }
        val ret = s[pos++]
        if (ret == '\n') {
            col = 0
            row++
        } else {
            col++
        }
        return ret
    }

    override fun peek(): Char? {
        if (peeked != null) {
            return peeked!!
        }
        peeked = pop()
        return peeked
    }
}