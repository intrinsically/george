package costanza.reflect


interface IProvider {
    fun skip()
    fun pop(): Char?
    fun peek(): Char?
    fun popInt(): Int
    fun popDouble(): Double
    fun popString(): String
    fun popName(): String
    fun popChar(char: Char)
}

