package costanza.geometry

class Router(val a: Rect, val b: Rect, points: List<Coord> = listOf(), parentOffset: Coord) {
    private var coords: Array<Coord> = arrayOf()
    init {
        coords = Array(points.size) { index ->
            points[index] + parentOffset
        }
    }
    private var route: Array<Coord>? = null
    
    /**
     * route as a polyline, but aim to go straight to the edge of the rect if possible
     */
    fun route(): Array<Coord> {
        if (route === null) {
            val through =
                    if (coords.isEmpty()) arrayOf(a.center.mid(b.center)) else coords

            route = arrayOf(a.intersect(through.first()), *coords, b.intersect(through.last()))
        }
        return route!!
    }

    fun startText(height: Double, width: Double, marker: Boolean, linesAway: Int) = adjust(a, route()[0], route()[1], height, width, marker, linesAway)
    fun endText(height: Double, width: Double, marker: Boolean, linesAway: Int) = adjust(b, route().last(), route()[route().size - 2], height, width, marker, linesAway)

    private fun adjust(r: Rect, c: Coord, next: Coord, height: Double, width: Double, marker: Boolean, linesAway: Int): Coord {
        val arrow = if (marker) 20.0 else 0.0
        val hOffset = linesAway * (height + 2)

        // if square, adjust based on the side you sit
        when (r.canBeSquare(next)) {
            0 -> return c + Dim(5.0, - height - arrow - hOffset)
            1 -> return c + Dim(5.0 + arrow, 3 - hOffset - linesAway * 6)
            2 -> return c + Dim(5.0, arrow + hOffset)
            3 -> return c + Dim(-7.0 - arrow - width, 3 - hOffset - linesAway * 6)
        }

        // otherwise it's on a slope, take into account the angle of the entry
        return when {
            r.inTop(c) && c.x < r.center.x ->
                c + Dim(5.0, - 5.0 - height - hOffset)
            r.inTop(c) ->
                c + Dim(-5.0 - width, -5.0 - height - hOffset)
            r.inRight(c) && c.y < r.center.y ->
                c + Dim(5.0, -height - 20.0 - hOffset)
            r.inRight(c) ->
                c + Dim(5.0, 20.0 + hOffset)
            r.inBottom(c) && c.x < r.center.x ->
                c + Dim(- 5.0 - arrow - width - linesAway * 20.0, 5.0 + hOffset)
            r.inBottom(c) ->
                c + Dim(arrow + linesAway * 20.0, 5.0 + hOffset)
            r.inLeft(c) && c.y < r.center.y ->
                c + Dim(-10.0 - width, 5.0 + hOffset)
            r.inLeft(c) ->
                c + Dim(-10.0 - width, -height - 5.0 - hOffset)
            else -> c
        }
    }

    fun bounds(): Rect {
        route()
        var rect: Rect? = null
        route!!.forEach {
            val curr = Rect(it, Dim(0,0))
            if (rect !== null) rect = rect!! + curr else rect = curr
        }
        return rect ?: Rect(0,0,0,0)
    }
}