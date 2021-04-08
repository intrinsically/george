package costanza.diagrams.art

import com.github.nwillc.ksvg.elements.SVG
import costanza.diagrams.base.BasicBox
import costanza.diagrams.base.Container
import costanza.diagrams.classpalette.Association
import costanza.geometry.Coord
import costanza.geometry.Rect
import diagrams.base.Box
import diagrams.base.Diagram
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("circle")
class Circle: BasicBox() {
    var cx: Double = 0.0
    var cy: Double = 0.0
    var radius: Double = 10.0

    override fun bounds(diagram: Diagram) = Rect(cx - radius, cy - radius, radius * 2, radius * 2) + parentOffset

    override fun add(diagram: Diagram, svg: SVG, zIndex: Int) {
        if (this.zIndex != zIndex) {
            return
        }
        val x = cx; val y = cy
        svg.circle {
            cx = "${x + parentOffset.x}"; cy = "${y + parentOffset.y}"; this.r = "$radius"
            stroke = "red"
            fill = "white"
        }
    }
}

fun Container.circle(block: (Circle.() -> Unit)? = null): Circle {
    val circle = Circle()
    shapes.add(circle)
    if (block !== null) {
        circle.apply(block)
    }
    return circle
}