package costanza.george.ui.palette

import costanza.george.capabilities.ICreateBox
import costanza.george.diagrams.base.Box
import costanza.george.geometry.Coord
import react.RBuilder


class Maker(val name: String, val boxMaker: ICreateBox, val icon: RBuilder.() -> Unit)