package costanza.utility


/** immutable collections */
fun<T> list(vararg args: T) = listOf(*args)
fun<T> array(vararg args: T) = args
fun<T> set(vararg args: T) = setOf(*args)
fun<A,B> map(vararg args: Pair<A,B>) = mapOf(*args)

/** mutable variants */
fun<T> _list(vararg args: T) = mutableListOf(*args)
fun<T> _set(vararg args: T) = mutableSetOf(*args)
fun<A,B> _map(vararg args: Pair<A,B>) = mutableMapOf(*args)

/** allow a capitalized variant for types */
typealias _List<T> = MutableList<T>
typealias _Set<T> = MutableSet<T>
typealias _Map<A,B> = MutableMap<A,B>

