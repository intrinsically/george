import antd.dropdown.dropdown
import antd.menu.menu
import antd.menu.menuItem
import antd.menu.subMenu
import antd.tree.tree
import costanza.Together
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
                        menuItem {
                            attrs {
                                key = "1"
                            }
                            +"Hello"
                        }
                        subMenu {
                            attrs {
                                key = "4"
                                title = "Submenu"
                            }
                            menuItem {
                                attrs {
                                    key = "2"
                                }
                                +"One"
                            }
                            menuItem {
                                attrs {
                                    key = "3"
                                }
                                +"Two"
                            }
                        }
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

