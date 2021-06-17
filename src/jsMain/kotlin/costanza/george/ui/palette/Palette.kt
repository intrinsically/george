package costanza.george.ui.palette

import antd.icon.poweroffOutlined
import antd.icon.searchOutlined
import costanza.george.diagrams.base.Box
import costanza.george.diagrams.classpalette.Klass
import costanza.george.geometry.Coord
import costanza.george.geometry.Dim
import costanza.george.utility.list
import react.RBuilder


/** a palette is a collection of makers inside a collapsible compartment on the screen */
class Palette(val name: String, val makers: List<Maker>)
