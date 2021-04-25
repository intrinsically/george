package costanza.diagrams.base

import costanza.reflect.IReflect
import costanza.reflect.ReflectInfo
import costanza.reflect.reflect
import costanza.reflect.typedproperties.optionalString


abstract class Part: IReflect {
    override fun reflectInfo(): ReflectInfo =
        reflect("part") {
            optionalString("details", true, { details }, { details = it })
        }
    /** details to display on the screen */
    var details: String? = null

    /** get the shape type, allowing this to be overridden */
    open fun type() = this::class.simpleName ?: ""
}