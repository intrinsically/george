package costanza.george.diagrams.base

import costanza.george.reflect.IReflect
import costanza.george.reflect.ReflectInfo
import costanza.george.reflect.reflect
import costanza.george.reflect.typedproperties.optionalString


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