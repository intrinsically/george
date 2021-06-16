import antd.ReactNode
import antd.tree.DataNode
import costanza.george.diagrams.base.Container
import costanza.george.diagrams.base.Diagram
import costanza.george.diagrams.base.Shape
import kotlinext.js.jsObject


/** simple dsl for making tree nodes */
fun tnode(ptitle: ReactNode?, pkey: Any, block: (DataNode.() -> Unit)? = null): DataNode {
    val node = jsObject<DataNode> { title = ptitle; key = pkey }
    if (block != null) {
        node.apply(block)
    }
    return node
}

/** synchronize between a diagram and the tree, in both directions */
class TreeSynchronizer(val diagram: Diagram) {
    var treeData: Array<DataNode>
    private var key = 0

    init {
        // turn the diagram into a nested set of nodes, with keys
        val top = tnode("", key++)
        addChildren(top, diagram)
        treeData = top.children!!
    }

    private fun addChildren(node: DataNode, c: Container) {
        val childs = mutableListOf<DataNode>()
        node.children?.forEach { childs.add(it) }
        c.shapes.forEach {
            childs.add(makeChild(it))
        }
        node.children = childs.toTypedArray()
    }

    private fun makeChild(shape: Shape): DataNode {
        val node = tnode(title(shape.type(), shape.name), key++)

        // add parts first
        if (shape.collectParts() != null) {
            addParts(node, shape)
        }

        if (shape is Container) {
            addChildren(node, shape)
        }
        return node
    }

    private fun addParts(node: DataNode, shape: Shape) {
        val childs = mutableListOf<DataNode>()

        shape.collectParts()?.forEach {
            childs.add(tnode(title(it.type(), it.details), key++))
        }
        node.children = childs.toTypedArray()
    }

    private fun title(type: String, details: String?) = details ?: type
}