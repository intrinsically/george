import antd.layout.*
import kotlinx.css.*
import kotlinx.css.properties.LineHeight
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.div
import styled.StyleSheet
import styled.css
import styled.styledDiv

object LayoutStyles : StyleSheet("layout", isStatic = true) {
    val basic by css {
        descendants(".ant-layout") {
            textAlign = TextAlign.left
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
            background = "#3ba0e9"
            color = Color.white
            lineHeight = LineHeight("120px")
            minWidth = 900.0.px
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
                    header { +"Header" }
                    layout {
                        sider {
                            treeview {
                            }
                        }
                        content {
                            diagramview {
                            }
                        }
                    }
                    footer { +"Footer" }
                }
            }
        }
    }
}
