package costanza.george.ui.palette

import costanza.george.capabilities.ICreateBox
import react.RBuilder


class Maker(val name: String, val boxMaker: ICreateBox, val icon: RBuilder.() -> Unit)