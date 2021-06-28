package costanza.george.diagrams.classpalette

import costanza.george.diagrams.base.Part

class Attribute(): Part() {
    override val objectType = "attribute"

    constructor(details: String): this() {
        this.details = details
    }
}
