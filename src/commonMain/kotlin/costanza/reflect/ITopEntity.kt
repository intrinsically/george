package costanza.reflect

import kotlin.random.Random

interface ITopEntity: IEntity {
    fun find(id: String): IEntity?
    /** make an id which is unique but ideally simple */
    fun makeId(): String
}

/** make a random 3 letter prefix, add the incrementing code to this */
fun makePrefix(): String = (0..2).fold("") { start, _ ->
        start + Random.Default.nextInt('a'.code, 'z'.code).toChar()
    }
