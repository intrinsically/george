package costanza.diagrams.classpalette

import costanza.diagrams.base.Part

class Attribute(): Part() {
    constructor(details: String): this() {
        this.details = details
    }
    override fun type() = "Attribute"
}
