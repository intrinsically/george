import antd.layout.*
import kotlinext.js.js
import kotlinx.css.*
import kotlinx.css.properties.LineHeight
import kotlinx.html.style
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.div
import react.dom.style
import styled.StyleSheet
import styled.css
import styled.styledDiv

object LayoutStyles : StyleSheet("layout", isStatic = true) {
    val basic by css {
        descendants(".ant-layout") {
            textAlign = TextAlign.left
            height = 100.vh
            width = 100.vw
        }
        descendants(".ant-layout-header") {
            background = "#7dbcea"
            color = Color.white
        }
        descendants(".ant-layout-content") {
            minHeight = 120.px
            background = rgba(255, 255, 255, 1.0).toString()
            color = Color.white
            lineHeight = LineHeight("120px")
        }
        descendants(".ant-layout-footer") {
            background = "#7dbcea"
            color = Color.white
        }
        descendants(".ant-layout-sider") {
            background = "#ffffff"
            color = Color.white
            borderRight = "#e0e0e0"
            borderStyle = BorderStyle.solid
            borderWidth = 1.px
        }
    }
    val overlay by css {
        descendants {
            cursor = Cursor.crosshair
            overflow = Overflow.scroll
        }
    }
}


@JsExport
class ScreenLayout(props: RProps) : RComponent<RProps, RState>(props) {
    override fun RBuilder.render() {
        styledDiv {
            css { +LayoutStyles.basic }
            div {
                layout {
                    header { +"Header XYZ" }
                    layout {
                        sider {
                            attrs {
                                width = 250.px
                            }
                            treeview {
                            }
                        }
                        content {
                            styledDiv {
                                css { +LayoutStyles.overlay }
                                diagramview {
                                }
                            }
                        }
                    }
                    footer { +"Footer" }
                }
            }
        }
    }
}
