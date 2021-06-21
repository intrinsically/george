package costanza.george.diagrams.classpalette

import costanza.george.diagrams.base.Part
import costanza.george.reflect.ReflectInfo
import costanza.george.reflect.reflect

class Attribute(): Part() {
    override fun entityType() = "attribute"
    override fun type() = "Attribute"

    constructor(details: String): this() {
        this.details = details
    }
}
