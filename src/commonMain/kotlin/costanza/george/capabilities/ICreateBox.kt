package costanza.george.capabilities

import costanza.george.diagrams.base.Box
import costanza.george.geometry.Coord

fun interface ICreateBox {
    fun create(loc: Coord): Box
}