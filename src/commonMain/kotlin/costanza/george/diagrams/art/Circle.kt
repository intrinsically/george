package costanza.george.diagrams.art

import costanza.george.diagrams.base.BasicBox
import costanza.george.diagrams.base.Container
import costanza.george.diagrams.base.Diagram
import costanza.george.geometry.Rect
import costanza.george.reflect.typedproperties.DoubleProperty
import costanza.george.reflect.typedproperties.StringProperty
import ksvg.elements.SVG

class Circle: BasicBox() {
    override fun entityType() = "circle"

    var cx: Double = 0.0
    var prop_cx = DoubleProperty(this, "cx", false, 0.0, { cx }, { cx = it })
    var cy: Double = 0.0
    var prop_cy = DoubleProperty(this, "cy", false, 0.0, { cy }, { cy = it })
    var radius: Double = 10.0
    var prop_radius = DoubleProperty(this, "radius", false, 10.0, { radius }, { radius = it })
    var fill: String = "orange"
    var prop_fill = StringProperty(this, "fill", false, "orange", { fill }, { fill = it })

    override fun bounds(diagram: Diagram) = Rect(cx - radius, cy - radius, radius * 2, radius * 2) + parentOffset

    override fun add(diagram: Diagram, svg: SVG, zIndex: Int) {
        if (this.zIndex != zIndex) {
            return
        }
        val x = cx; val y = cy
        val cfill = fill
        svg.circle {
            cx = "${x + parentOffset.x}"; cy = "${y + parentOffset.y}"; this.r = "$radius"
            stroke = "grey"
            fill = cfill
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