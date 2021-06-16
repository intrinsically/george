package costanza.george.diagrams.classpalette

import costanza.george.diagrams.base.Part
import costanza.george.reflect.ReflectInfo
import costanza.george.reflect.reflect
import costanza.george.reflect.typedproperties.optionalString

class Operation(): Part() {
    constructor(details: String): this() {
        this.details = details
    }

    override fun reflectInfo(): ReflectInfo =
        reflect("operation", super.reflectInfo()) {
        }

    override fun type() = "Operation"
}