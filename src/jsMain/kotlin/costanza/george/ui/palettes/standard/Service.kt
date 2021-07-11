package costanza.george.ui.palettes.standard

import antd.icon.poweroffOutlined
import antd.icon.searchOutlined
import costanza.george.diagrams.classpalette.Klass
import costanza.george.geometry.Dim
import costanza.george.ui.palette.Maker
import costanza.george.ui.palette.Palette
import costanza.george.utility.list

val servicePalette = Palette("Service diagrams",
    list(
        Maker("Service", { loc -> Klass(loc, Dim(150,0)) }) { poweroffOutlined {} },
        Maker("Database", { loc -> Klass(loc) }) { searchOutlined {} },
        Maker("Cache", { loc -> Klass(loc) }) { poweroffOutlined {} },
        Maker("Queue", { loc -> Klass(loc, Dim(150, 0)) }) { poweroffOutlined {} },
        Maker("Disk", { loc -> Klass(loc, Dim(150, 0)) }) { poweroffOutlined {} },
    )
)