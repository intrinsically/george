package com.github.nwillc.ksvg.elements

import com.github.nwillc.ksvg.attributes.*
import com.github.nwillc.ksvg.attributes.AttributeProperty

/**
 * An SVG line element.
 */
class POLYLINE(validation: Boolean = false, hasAttributes: HasAttributes = HasAttributesImpl(validation)) :
    Element("polyline", validation, hasAttributes),
    HasStroke by HasStrokeImpl(hasAttributes),
    HasFill by HasFillImpl(hasAttributes) {

    /**
     * The points defining the x and y coordinates for each part of the line
     */
    var points: String? by AttributeProperty(type = AttributeType.Path)

    /**
     * The start marker for the line
     */
    var markerStart: String? by AttributeProperty(type = AttributeType.None)

    /**
     * The mid marker for the line
     */
    var markerMid: String? by AttributeProperty(type = AttributeType.None)

    /**
     * The end marker for the line
     */
    var markerEnd: String? by AttributeProperty(type = AttributeType.None)

    /**
     * dash type
     */
    var strokeDasharray: String? by AttributeProperty(type = AttributeType.NumberList)
}
