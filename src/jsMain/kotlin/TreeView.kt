import antd.icon.*
import antd.input.textArea
import antd.tree.TreeNodeAttribute
import antd.tree.tree
import antd.tree.treeNode
import costanza.app.Together
import diagrams.base.Diagram
import kotlinx.html.unsafe
import react.*
import react.dom.div
import styled.StyleSheet
import styled.css
import styled.styledDiv

external interface TreeProps : RProps {
}

class TreeState() : RState

object TreeStyles : StyleSheet("tree", isStatic = true) {
    val container by css {}
    val basic by css {}
    val customizedIcon by css {}
    val basicControlled by css {}
    val dynamic by css {}
    val line by css {}
    val directory by css {}
}

@JsExport
class TreeView(props: TreeProps) : RComponent<TreeProps, TreeState>(props) {
    override fun RBuilder.render() {
        styledDiv {
            css { +TreeStyles.customizedIcon }
            tree {
                attrs {
                    showIcon = true
                    defaultExpandAll = true
                    defaultSelectedKeys = arrayOf("0-0-0")
                    switcherIcon = buildElement {
                        downOutlined {}
                    }
                }
                treeNode {
                    attrs {
                        icon = buildElement {
                            smileOutlined {}
                        }
                        title = "parent 1"
                        key = "0-0"
                    }
                    treeNode {
                        attrs {
                            icon = buildElement {
                                mehOutlined {}
                            }
                            title = "leaf"
                            key = "0-0-0"
                        }
                    }
                    treeNode {
                        attrs {
                            icon = fun(treeNode: TreeNodeAttribute): ReactElement {
                                return buildElement {
                                    if (treeNode.selected) {
                                        frownFilled {}
                                    } else frownOutlined {}
                                }
                            }
                            title = "leaf"
                            key = "0-0-1"
                        }
                    }
                }
            }
        }
    }
}

fun RBuilder.treeview(handler: TreeProps.() -> Unit): ReactElement {
    return child(TreeView::class) {
        this.attrs(handler)
    }
}

