package costanza.george.reflect

/** an object implementing this can provide reflection information about itself */
interface IObject {
    fun reflectInfo(): ReflectInfo
}