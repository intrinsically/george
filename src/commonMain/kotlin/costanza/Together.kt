package costanza

import costanza.diagrams.art.circle
import costanza.diagrams.base.ITextCalculator
import costanza.diagrams.classpalette.*
import costanza.diagrams.drawingEntityTypes
import costanza.diagrams.infopalette.area
import costanza.diagrams.infopalette.note
import costanza.geometry.Coord
import costanza.geometry.Dim
import costanza.reflect.EntityTypeRegistry
import costanza.reflect.TokenProvider
import costanza.reflect.operations.Deserializer
import costanza.reflect.operations.Serializer
import costanza.utility._list
import costanza.diagrams.base.Diagram
import costanza.diagrams.base.diagram
import ksvg.RenderMode
import ksvg.elements.SVG

class Together {
    fun makeDiagram2(calc: ITextCalculator) =
        diagram(calc, "Test") {
            val width = 164
            val height = 84
            circle {
                cx = 600.0
                cy = 670.0
                radius = 7.0
            }
            circle {
                cx = 600.0 + width
                cy = 670.0
                radius = 7.0
            }
            circle {
                cx = 600.0 + width
                cy = 670.0 + height
                radius = 7.0
            }
            circle {
                cx = 600.0
                cy = 670.0 + height
                radius = 7.0
            }
        }

    fun makeDiagram3(calc: ITextCalculator) =
        diagram(calc, "Test") {
            area {
                loc = Coord(1220, 1350)
                dim = Dim(200, 100)
            }
            area {
                loc = Coord(1220 + 20, 1350 + 20)
                dim = Dim(200 - 40, 100 - 40)
            }
        }

    fun makeDiagram(calc: ITextCalculator) =
        diagram(calc, "Test") {
            note {
                loc = Coord(585, 900)
                text = "This is a note, i hope it will word wrap..."
            }
            area("Area 1") {
                loc = Coord(100, 900)
                dim = Dim(100, 100)
                area("Area 2") {
                    loc = Coord(20, 50)
                    dim = Dim(300, 100)

                    klass("TestClass") {
                        loc = Coord(50,50)
                        attribute("+a: Int")
                        attribute("+name: String")
                        operation("printIt(): void")
                        operation("hashCode(): long")
                    }
                }
            }
            inheritance("TestClass", "RenderLogic") {
                points = _list(Coord(330, 800))
            }
            area("My Area") {
                loc = Coord(220, 100)
                dim = Dim(650, 450)
                klass("WidgetFactory") {
                    loc = Coord(50,50)
                    attribute("+a: Int")
                    attribute("+name: String")
                    operation("printIt(): void")
                    operation("hashCode(): long")
                }
                klass("ApplicationManager") {
                    loc = Coord(400,50)
                    stereotype = "data-class"
                    attribute("+a: Int")
                    attribute("+name: String")
                }
                klass("RenderLogic") {
                    loc = Coord(50,250)
                }
                klass("DisplayLogic") {
                    loc = Coord(400,270)
                    operation("printIt(): void")
                    operation("hashCode(): long")
                }
                inheritance("DisplayLogic", "ApplicationManager")
                association("ApplicationManager", "DisplayLogic") {
                    composition = CompositionType.AGGREGATION
                    startLabel = "+a"
                    startMult = "0..*"
                    label = "association"
                    arrow = true
                    endLabel = "andrew"
                    endMult = "1..4"
                    points = _list(Coord(420, 200))
                }
                association("DisplayLogic", "WidgetFactory") {
                    composition = CompositionType.COMPOSITION
                    startLabel = "+a"
                    startMult = "0..*"
                    label = "association"
                    endLabel = "andrew"
                    endMult = "1..4"
                    points = _list(Coord(350, 280))
                }
            }
            klass("Outside") {
                loc = Coord(600,670)
                operation("printIt(): void")
                operation("hashCode(): long")
            }
            inheritance("DisplayLogic", "Outside")
        }

    fun makeSVG(diag: Diagram): String {
        val svg = SVG()
        diag.add(svg)
        val buffer = StringBuilder()
        val bounds = diag.bounds(diag)
        svg.width = "${bounds.x2}"
        svg.height = "${bounds.y2}"
        svg.render(buffer, RenderMode.INLINE)
        return buffer.toString()
    }

    fun serialize(diag: Diagram) = Serializer().serialize(diag)

    fun makeDiag(calc: ITextCalculator, serial: String): Diagram {
        val diag = diagram(calc, "diagram") {}
        val reg = EntityTypeRegistry()
        reg.addAll(drawingEntityTypes)
        val deser = Deserializer(reg)
        deser.deserialize(diag, TokenProvider(serial))
        return diag
    }
}

