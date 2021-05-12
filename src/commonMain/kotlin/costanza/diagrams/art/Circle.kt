package costanza.diagrams.art

import ksvg.elements.SVG
import costanza.diagrams.base.BasicBox
import costanza.diagrams.base.Container
import costanza.geometry.Rect
import costanza.reflect.ReflectInfo
import costanza.reflect.entityList
import costanza.reflect.reflect
import costanza.reflect.typedproperties.double
import diagrams.base.Diagram

class Circle: BasicBox() {

    override fun reflectInfo(): ReflectInfo =
        reflect("circle", super.reflectInfo()) {
            double("cx", false, 0.0, { cx }, { cx = it })
            double("cy", false, 0.0, { cy }, { cy = it })
            double("raduis", false, 10.0, { radius }, { radius = it })
        }

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
            stroke = "grey"
            fill = "orange"
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