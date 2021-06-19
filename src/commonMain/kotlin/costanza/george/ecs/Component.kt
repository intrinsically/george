package costanza.george.ecs

import costanza.george.reflect.ReflectInfo

abstract class Component(entity: Entity): ReflectInfo("") {
    init {
        entity.add(this)
    }
}
