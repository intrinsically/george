package costanza.george.geometry

data class Dim(val width: Double, val height: Double) {
    constructor(width: Int, height: Int): this(width.toDouble(), height.toDouble())
}