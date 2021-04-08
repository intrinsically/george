package costanza.app

import costanza.diagrams.base.FontDetails
import costanza.diagrams.base.ITextCalculator
import java.awt.Font
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import kotlin.math.max

/** translate to a java font */
fun FontDetails.makeFont() = Font(face, if (bold) Font.BOLD else Font.PLAIN, size)

class G2DTextCalculator: ITextCalculator {
    private var g2d: Graphics2D? = null

    private fun getG2D(): Graphics2D {
        if (g2d == null) {
            val fmImage = BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB)
            g2d = fmImage.createGraphics()
        }
        return g2d!!
    }

    /** prepare by actually doing a calculation - takes about a second to set up, from then on calculations are fast */
    fun prepare() {
        getG2D()
        calcHeight(FontDetails.NAME)
    }

    /** the actual height of the text */
    override fun calcHeight(details: FontDetails) =
        getG2D().getFontMetrics(details.makeFont()).height.toDouble()

    /** the width of the string - usually a bit more than actual due to kerning */
    override fun calcWidth(details: FontDetails, minWidth: Double, text: String): Double =
        max(minWidth, getG2D().getFontMetrics(details.makeFont()).stringWidth(text).toDouble() * 1.1)
}
