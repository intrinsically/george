package com.github.nwillc.ksvg.elements

import com.github.nwillc.ksvg.attributes.*
import com.github.nwillc.ksvg.attributes.AttributeProperty

class MARKER(validation: Boolean = false, hasAttributes: HasAttributes = HasAttributesImpl(validation)) :
    Container("marker", validation, hasAttributes) {

    var viewBox: String? by AttributeProperty("viewBox", type = AttributeType.NumberList)
    var orient: String? by AttributeProperty(type = AttributeType.None)
    var markerWidth: String? by AttributeProperty("markerWidth", type = AttributeType.LengthOrPercentage)
    var markerHeight: String? by AttributeProperty("markerHeight", type = AttributeType.LengthOrPercentage)
    var refX: String? by AttributeProperty("refX", type = AttributeType.LengthOrPercentage)
    var refY: String? by AttributeProperty("refY", type = AttributeType.LengthOrPercentage)
}
