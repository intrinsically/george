package costanza.george.ui.palette

import antd.icon.poweroffOutlined
import antd.icon.searchOutlined
import costanza.george.diagrams.classpalette.Klass
import costanza.george.geometry.Dim
import costanza.george.utility.list

val servicePalette = Palette("Service diagrams",
    list(
        Maker("Service", { loc -> Klass(loc, Dim(150,0)) }) { poweroffOutlined {} },
        Maker("Database", { loc -> Klass(loc) }) { searchOutlined {} },
        Maker("Cache", { loc -> Klass(loc) }) { poweroffOutlined {} },
    )
)