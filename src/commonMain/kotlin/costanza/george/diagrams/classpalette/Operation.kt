package costanza.george.diagrams.classpalette

import costanza.george.diagrams.base.Part

class Operation(): Part() {
    override fun entityType() = "operation"

    constructor(details: String): this() {
        this.details = details
    }
}