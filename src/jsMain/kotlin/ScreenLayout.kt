import antd.layout.*
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.css.*
import org.w3c.dom.Screen
import org.w3c.dom.events.Event
import org.w3c.dom.events.MouseEvent
import react.*
import react.dom.div
import react.dom.style
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
            background = "white"
            cursor = Cursor.grab
        }
        descendants(".ant-layout-content .overlay") {
            cursor = Cursor.crosshair
            position = Position.absolute
            display = Display.block
            height = 100.pc
            width = 100000.px
            background = rgba(0,0,0,0.0).toString()
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
    val overlay by css {
        descendants {
            cursor = Cursor.crosshair
            overflow = Overflow.scroll
        }
    }
}

data class ScreenLayoutState(var scrollTop: Int = 0, var scrollLeft: Int = 0): RState


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
                        sider {
                            attrs {
                                width = 250.px
                            }
                            treeview {
                            }
                        }
                        diagramview {
                        }
                    }
                    footer { +"Footer" }
                }
            }
        }
    }
}
