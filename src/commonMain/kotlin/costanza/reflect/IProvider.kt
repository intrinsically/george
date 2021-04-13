package costanza.reflect

interface IProvider {
    fun next(): String?
}

class StringProvider(s: String): IProvider {
    private val parts = s.split("~").iterator()

    override fun next(): String? = if (parts.hasNext()) { parts.next() } else { null }
}