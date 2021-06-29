package costanza.george.ecs

abstract class Component(entity: Entity) {
    init {
        entity.add(this)
    }
}
