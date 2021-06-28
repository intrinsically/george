import antd.dropdown.dropdown
import antd.menu.menu
import antd.menu.menuItem
import antd.menu.subMenu
import antd.tree.tree
import costanza.george.diagrams.Together
import react.*
import styled.StyleSheet
import styled.css
import styled.styledDiv

external interface TreeProps : RProps {
}

class TreeState() : RState

object TreeStyles : StyleSheet("tree", isStatic = true) {
    val basic by css {}
}


class TreeView(props: TreeProps) : RComponent<TreeProps, TreeState>(props) {
    val together = Together()
    val calc = ClientTextCalculator()
    val tsync = TreeSynchronizer(together.makeDiagram(calc))

    override fun RBuilder.render() {
        dropdown {
            attrs {
                overlay = buildElement {
                    menu {
                    }
                }
                trigger = arrayOf("contextMenu")
            }
            styledDiv {
                css { +TreeStyles.basic }
                tree {
                    attrs {
                        showIcon = false
                        showLine = true
                        draggable = true
                        treeData = tsync.treeData
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

