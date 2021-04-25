package costanza.utility

/** loop with no index */
fun Int.loop(block: () -> Unit) {
    (1..this).forEach { _ -> block() }
}

/** loop with index */
fun Int.iloop(block: (index: Int) -> Unit) {
    (0 until this).forEach { block(it) }
}

fun IntRange.loop(block: () -> Unit) {
    this.forEach { _ -> block.apply {  } }
}