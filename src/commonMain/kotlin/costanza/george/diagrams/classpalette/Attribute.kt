package costanza.george.diagrams.classpalette

import costanza.george.diagrams.base.Part
import costanza.george.reflect.ReflectInfo
import costanza.george.reflect.reflect

class Attribute(): Part() {
    constructor(details: String): this() {
        this.details = details
    }

    override fun reflectInfo(): ReflectInfo =
        reflect("attribute", super.reflectInfo()) {
        }

    override fun type() = "Attribute"
}
