package costanza.george.diagrams.classpalette

import costanza.george.diagrams.base.Part

class Attribute(): Part() {
    override fun entityType() = "attribute"

    constructor(details: String): this() {
        this.details = details
    }
}
