package costanza.george.reflect

/** an entity is an object, it has properties which can either be entities or primitives */
interface IReflect {
    fun reflectInfo(): ReflectInfo
}