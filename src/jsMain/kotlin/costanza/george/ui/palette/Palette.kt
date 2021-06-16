package costanza.george.ui.palette

import antd.icon.poweroffOutlined
import antd.icon.searchOutlined
import costanza.george.diagrams.base.Box
import costanza.george.diagrams.classpalette.Klass
import costanza.george.geometry.Coord
import costanza.george.geometry.Dim
import costanza.george.utility.list
import react.RBuilder

class Maker(val name: String, val boxMaker: (Coord) -> Box?, val icon: RBuilder.() -> Unit)

/** a palette is a collection of makers inside a collapsible compartment on the screen */
class Palette(val name: String, val makers: List<Maker>)

val service = Palette("Service diagrams",
    list(
        Maker("Service", { loc -> Klass(loc, Dim(150,0)) }) { poweroffOutlined {} },
        Maker("Database", { loc -> Klass(loc) }) { searchOutlined {} },
        Maker("Cache", { loc -> Klass(loc) }) { poweroffOutlined {} },
))
val shapes = Palette("Shapes",
    list(
        Maker("Circle", { loc -> Klass(loc) }) { poweroffOutlined {} },
        Maker("Square", { loc -> Klass(loc) }) { searchOutlined {} },
        Maker("Rectangle", { loc -> Klass(loc) }) { poweroffOutlined {} },
    ))

val defaultPalettes = list(service, shapes)