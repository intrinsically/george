package costanza.george.geometry

data class Dim(val width: Double = 0.0, val height: Double = 0.0) {
    constructor(width: Int, height: Int): this(width.toDouble(), height.toDouble())
}