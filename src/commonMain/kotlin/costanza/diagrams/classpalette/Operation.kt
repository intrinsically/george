package costanza.diagrams.classpalette

import costanza.diagrams.base.Part
import costanza.reflect.ReflectInfo
import costanza.reflect.reflect
import costanza.reflect.typedproperties.optionalString

class Operation(): Part() {
    constructor(details: String): this() {
        this.details = details
    }

    override fun reflectInfo(): ReflectInfo =
        reflect("operation", super.reflectInfo()) {
        }

    override fun type() = "Operation"
}