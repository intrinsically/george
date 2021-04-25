package costanza.diagrams.base

import ksvg.elements.TEXT

enum class FontDetails(val face: String, val size: Int, val bold: Boolean) {
    NAME("Helvetica", 18, true),
    SUB("Helvetica", 15, false),
    LABEL("Helvetica", 14, false),
    NOTES("Helvetica", 14, false);

    fun setSvgDetails(text: TEXT) {
        text.fontFamily = face
        text.fontSize = "$size"
        text.fontWeight = if (bold) "bold" else ""
    }
}

interface ITextCalculator {
    fun calcHeight(details: FontDetails): Double
    fun calcWidth(details: FontDetails, minWidth: Double, text: String): Double
}

