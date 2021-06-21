package costanza.george.diagrams.classpalette

import costanza.george.diagrams.base.Part
import costanza.george.reflect.ReflectInfo
import costanza.george.reflect.reflect
import costanza.george.reflect.typedproperties.optionalString

class Operation(): Part() {
    override fun entityType() = "operation"
    override fun type() = "Operation"

    constructor(details: String): this() {
        this.details = details
    }
}