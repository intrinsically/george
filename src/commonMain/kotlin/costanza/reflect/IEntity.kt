package costanza.reflect

interface IEntity {
    val entityName: String
    val properties: List<IProperty>
}