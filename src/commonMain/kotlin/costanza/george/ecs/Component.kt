package costanza.george.ecs

import costanza.george.reflect.ReflectInfo

abstract class Component(entity: Entity) {
    init {
        entity.add(this)
    }
}
