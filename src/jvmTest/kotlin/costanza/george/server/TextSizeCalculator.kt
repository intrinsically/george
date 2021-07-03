package costanza.george.server

import costanza.george.diagrams.base.FontDetails
import org.junit.Test


class TextSizeCalculator {
    @Test
    fun computeSizes() {
        val calc = G2DTextCalculator()
        calc.prepare()

        // keep an array of sizes
        val values = FontDetails.values()
        val size = FontDetails.values().size
        // compute heights
        val heights: Array<Double> = Array<Double>(size, {calc.calcHeight(values[it])})
        val widths: Array<Array<Double>> = Array<Array<Double>>(size) {
                index -> Array<Double>(256) {
                char -> calc.calcWidth(values[index], 0.0, Character.toString(char))}}

        // print it out so we can paste into the js calculator
        val data = heights.joinToString(", ")
        println("const val TEXT_HEIGHTS = arrayOf(${data})")
        println("const val TEXT_WIDTHS = arrayOf<Array<Double>>(")
        values.forEachIndexed { index, details ->
            val inside = widths[index].joinToString(", ")
            println("    arrayOf(${inside}),")
        }
        println(")")
    }
}