package costanza.george.diagrams.classpalette

import costanza.george.diagrams.base.Part

class Operation(): Part() {
    override val objectType = "operation"

    constructor(details: String): this() {
        this.details = details
    }
}