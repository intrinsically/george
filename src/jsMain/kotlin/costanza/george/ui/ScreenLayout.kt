package costanza.george.ui

import antd.button.button
import antd.collapse.collapse
import antd.collapse.collapsePanel
import antd.layout.content
import antd.layout.header
import antd.layout.layout
import antd.layout.sider
import costanza.george.diagrams.art.Circle
import costanza.george.ui.commands.CreateBoxTool
import costanza.george.ui.palette.Palette
import costanza.george.ui.palettes.standard.servicePalette
import costanza.george.utility.list
import kotlinx.css.*
import react.*
import react.dom.div
import styled.StyleSheet
import styled.css
import styled.styledDiv

object ScreenLayoutStyles : StyleSheet("layout", isStatic = true) {
    val basic by css {
        descendants(".ant-layout") {
            textAlign = TextAlign.left
            height = 100.vh
            overflow = Overflow.hidden
        }
        descendants(".ant-layout-header") {
            background = "#7dbcea"
            color = Color.white
        }
        descendants(".ant-layout-content") {
            minHeight = 120.px
            position = Position.relative
            overflow = Overflow.hidden
            width = 100.pc
            height = 100.pc
            background = "white"
        }
        descendants(".ant-layout-content .overlay") {
            position = Position.absolute
            display = Display.block
            height = 100.pc
            width = 100000.px
            background = rgba(0, 0, 0, 0.0).toString()
            zIndex = 2
        }
        descendants(".ant-layout-footer") {
            background = "#7dbcea"
            color = Color.white
        }
        descendants(".ant-layout-sider") {
            background = "#f8f8f8"
            zIndex = 4
            descendants(".ant-tree") {
                background = "#00000000"
            }
            descendants(".ant-tree-switcher") {
                background = "#00000000"
            }
        }
    }
}

data class ScreenLayoutState(var scrollTop: Int = 0, var scrollLeft: Int = 0, var cursor: String = "normal") : RState


@JsExport
class ScreenLayout(props: RProps) : RComponent<RProps, ScreenLayoutState>(props) {
    init {
        state = ScreenLayoutState()
    }

    override fun RBuilder.render() {
        styledDiv {
            css { +ScreenLayoutStyles.basic }
            div {
                layout {
                    header { +"Costanza - pretending to be an architect" }
                    layout {
                        attrs.style = kotlinext.js.js { cursor = "${state.cursor}" }
                        sider {
                            addPalettes(list(servicePalette))
                        }
                        content {
                            diagramview {
                                tool = CreateBoxTool { loc ->
                                    val shape = Circle()
                                    shape.cx = loc.x
                                    shape.cy = loc.y
                                    shape.radius = 20.0
                                    shape.fill = "red"
                                    shape
                                }
                            }
                        }
                        sider {
                            treeview {}
                        }
                    }
                }
            }
        }
    }

    private fun RBuilder.addPalettes(palettes: List<Palette>) {
        collapse {
            attrs {
                bordered = false
                defaultActiveKey = "0"
            }
            palettes.forEachIndexed { index, palette ->
                collapsePanel {
                    attrs {
                        header = palette.name
                        key = "$index"
                    }
                    palette.makers.forEach {
                        if (it.boxMaker != null) {
                            makeButton(it.icon, it.name)
                        }
                    }
                }
            }
        }
    }

    private fun RBuilder.makeButton(elementIcon: RBuilder.() -> Unit, name: String) =
        button {
            attrs {
                type = "text"
                icon = buildElement(elementIcon)
                +name
                this.onClick = { setState { cursor = "crosshair" } }
            }
        }
}
