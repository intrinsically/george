package costanza

import costanza.diagrams.base.ITextCalculator
import costanza.diagrams.classpalette.*
import costanza.diagrams.drawingEntityTypes
import costanza.diagrams.infopalette.area
import costanza.diagrams.infopalette.note
import costanza.geometry.Coord
import costanza.reflect.EntityTypeRegistry
import costanza.reflect.TokenProvider
import costanza.reflect.operations.Deserializer
import costanza.reflect.operations.Serializer
import costanza.utility._list
import diagrams.base.Diagram
import diagrams.base.diagram
import ksvg.RenderMode
import ksvg.elements.SVG

class Together {
    fun makeDiagram(calc: ITextCalculator) =
        diagram(calc, "Test") {
            note {
                x = 585.0; y = 900.0
                text = "This is a note, i hope it will word wrap..."
            }
            area("Area 1") {
                x = 100.0; y = 900.0; width = 100.0; height = 100.0
                area("Area 2") {
                    x = 20.0; y = 50.0; width = 300.0; height = 100.0

                    klass("TestClass") {
                        x = 50.0; y = 50.0
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
                x = 220.0; y = 100.0
                width = 650.0; height = 450.0
                klass("WidgetFactory") {
                    x = 50.0; y = 50.0
                    attribute("+a: Int")
                    attribute("+name: String")
                    operation("printIt(): void")
                    operation("hashCode(): long")
                }
                klass("ApplicationManager") {
                    x = 400.0; y = 50.0
                    stereotype = "data-class"
                    attribute("+a: Int")
                    attribute("+name: String")
                }
                klass("RenderLogic") {
                    x = 50.0; y = 250.0
                }
                klass("DisplayLogic") {
                    x = 400.0; y = 270.0
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
                x = 600.0; y = 670.0
                operation("printIt(): void")
                operation("hashCode(): long")
            }
            inheritance("DisplayLogic", "Outside")
        }

    fun makeSVG(diag: Diagram): String {
        val svg = SVG()
        diag.add(svg)
        val buffer = StringBuilder()
        svg.width = "${diag.bounds(diag).width + 100.0}"
        svg.height = "${diag.bounds(diag).height + 100.0}"
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

