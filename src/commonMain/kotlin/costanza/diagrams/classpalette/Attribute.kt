package costanza.diagrams.classpalette

import costanza.diagrams.base.Part
import kotlinx.serialization.SerialInfo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("attribute")
class Attribute(): Part() {
    constructor(details: String): this() {
        this.details = details
    }
    override fun type() = "Attribute"
}
