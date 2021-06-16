import antd.button.button
import antd.collapse.collapse
import antd.collapse.collapsePanel
import antd.icon.poweroffOutlined
import antd.icon.searchOutlined
import antd.input.search
import antd.layout.*
import antd.tooltip.tooltip
import costanza.george.ui.palette.Palette
import costanza.george.ui.palette.defaultPalettes
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
                    header { +"Header" }
                    layout {
                        attrs.style = kotlinext.js.js { cursor = "${state.cursor}" }
                        sider {
                            addPalettes(defaultPalettes)
                        }
                        content {
                            diagramview {
                            }
                        }
                        sider {
                            attrs {
                                width = 250.px
                            }
                            treeview {
                            }
                        }
                    }
                        //                    footer { +"Footer" }
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
