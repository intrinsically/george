package costanza.diagrams.classpalette

import costanza.diagrams.base.Part
import costanza.reflect.ReflectInfo
import costanza.reflect.reflect

class Attribute(): Part() {
    constructor(details: String): this() {
        this.details = details
    }

    override fun reflectInfo(): ReflectInfo =
        reflect("attribute", super.reflectInfo()) {
        }

    override fun type() = "Attribute"
}
