package costanza.diagrams.classpalette

import costanza.diagrams.base.Part
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("operation")
class Operation(): Part() {
    constructor(details: String): this() {
        this.details = details
    }
    override fun type() = "Operation"
}